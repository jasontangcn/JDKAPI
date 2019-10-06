package com.fruits.jdkapi.thread.httpserver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.OutputStreamWriter;
import java.io.ByteArrayOutputStream;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.net.URLConnection;
import java.net.Socket;
import java.util.NoSuchElementException;

import com.fruits.jdkapi.thread.util.FIFOStack;

public class HttpWorker extends Object {
	private static int nextWorkerID = 0;
	private int workerID;

	private Thread internalThread;
	private FIFOStack fifoStackIdleWorkers;
	private FIFOStack handoffBox = new FIFOStack(1);
	private File docRoot;

	private volatile boolean noStopRequested;

	public HttpWorker(File docRoot, int workerPriority, FIFOStack fifoStackIdleWorkers) {
		this.docRoot = docRoot;
		this.fifoStackIdleWorkers = fifoStackIdleWorkers;

		workerID = generateNextID();
		handoffBox = new FIFOStack(1);

		noStopRequested = true;
		internalThread = new Thread(new Runnable() {
			public void run() {
				try {
					runWork();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		internalThread.start();
	}

	private static synchronized int generateNextID() {
		int id = nextWorkerID;
		nextWorkerID++;
		return id;
	}

	public void processRequest(Socket s) throws InterruptedException {
		handoffBox.add(s);
	}

	private void runWork() {
		Socket socket = null;
		InputStream is = null;
		OutputStream os = null;

		while (noStopRequested) {
			try {
				fifoStackIdleWorkers.add(this);
				socket = (Socket) handoffBox.remove();
				is = socket.getInputStream();
				os = socket.getOutputStream();
				generateResponse(is, os);
				os.flush();
			} catch (IOException e) {
				System.err.println("I/O error while processing request, ignoring and adding back to idle queue - workerID = " + workerID);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			} finally {
				if (null != is) {
					try {
						is.close();
					} catch (IOException e) {
					} finally {
						is = null;
					}
				}

				if (null != os) {
					try {
						os.close();
					} catch (IOException e) {
					} finally {
						os = null;
					}
				}

				if (null != socket) {
					try {
						socket.close();
					} catch (IOException e) {
					} finally {
						socket = null;
					}
				}
			}
		}
	}

	private void generateResponse(InputStream in, OutputStream out) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String requestLine = reader.readLine();
		if ((requestLine == null) || (requestLine.length() < 1)) {
			throw new IOException("could not read request");
		}
		System.out.println("workerID=" + workerID + ",requestLine=" + requestLine);
		StringTokenizer st = new StringTokenizer(requestLine);
		String fileName = null;

		try {
			st.nextToken();
			fileName = st.nextToken();
		} catch (NoSuchElementException x) {
			throw new IOException("could not parse request line");
		}

		File requestedFile = generateFile(fileName);
		BufferedOutputStream buffOut = new BufferedOutputStream(out);
		if (requestedFile.exists()) {
			System.out.println("workerID=" + workerID + ",200 OK:" + fileName);
			int fileLen = (int) requestedFile.length();
			BufferedInputStream fileIn = new BufferedInputStream(new FileInputStream(requestedFile));

			String contentType = URLConnection.guessContentTypeFromStream(fileIn);
			byte[] headerBytes = createHeaderBytes("HTTP/1.0 200 OK", fileLen, contentType);
			buffOut.write(headerBytes);

			byte[] buf = new byte[2048];
			int blockLen = 0;

			while ((blockLen = fileIn.read(buf)) != -1) {
				buffOut.write(buf, 0, blockLen);
			}
			fileIn.close();
		} else {
			System.out.println("workerID=" + workerID + ",404 Not Found:" + fileName);
			byte[] headerBytes = createHeaderBytes("HTTP/1.0 404 Not Found", -1, null);
			buffOut.write(headerBytes);
		}
		buffOut.flush();
	}

	private File generateFile(String fileName) {
		File requestedFile = docRoot;

		StringTokenizer st = new StringTokenizer(fileName, "/");
		while (st.hasMoreTokens()) {
			String tok = st.nextToken();
			if (tok.equals("..")) {
				continue;
			}
			requestedFile = new File(requestedFile, tok);
		}
		if (requestedFile.exists() && requestedFile.isDirectory()) {
			requestedFile = new File(requestedFile, "index.html");
		}

		return requestedFile;
	}

	private byte[] createHeaderBytes(String resp, int contentLen, String contentType) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(baos));

		writer.write(resp + "\r\n");
		if (contentLen != -1) {
			writer.write("Content-Length:" + contentLen + "\r\n");
		}

		if (contentType != null) {
			writer.write("Content-Type:" + contentType + "\r\n");
		}

		writer.write("\r\n");
		writer.flush();

		byte[] data = baos.toByteArray();
		writer.close();
		return data;
	}

	public void stopRequest() {
		noStopRequested = false;
		internalThread.interrupt();

	}

	public boolean isAlive() {
		return internalThread.isAlive();
	}
}