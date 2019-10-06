package com.fairchild.jdkapi.thread.httpserver;

import java.io.File;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;

import com.fairchild.jdkapi.thread.util.FIFOStack;

public class HttpServer extends Object {
	private FIFOStack fifoStackIdleWorkers;
	private HttpWorker[] workerList;
	private Thread internalThread;

	private ServerSocket serverSocket;
	private volatile boolean noStopRequest;

	public HttpServer(File docRoot, int port, int numberOfWorkers, int maxPriority) throws IOException {
		serverSocket = new ServerSocket(port, 10);

		if ((null == docRoot) || !docRoot.exists() || !docRoot.isDirectory())
			throw new IOException("Specified docRoot is NULL or does not exites or is not a dierectory.");

		numberOfWorkers = Math.max(1, numberOfWorkers);

		int serverPriority = Math.max(Thread.MIN_PRIORITY + 2, Math.min(maxPriority, Thread.MAX_PRIORITY - 1));

		int workerPriority = serverPriority - 1;

		fifoStackIdleWorkers = new FIFOStack(numberOfWorkers);
		workerList = new HttpWorker[numberOfWorkers];
		for (int i = 0; i < numberOfWorkers; i++)
			workerList[i] = new HttpWorker(docRoot, workerPriority, fifoStackIdleWorkers);

		noStopRequest = true;

		internalThread = new Thread(new Runnable() {
			public void run() {
				try {
					runWork();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		internalThread.setPriority(serverPriority);
		internalThread.start();

	}

	private void runWork() {
		System.out.println("HttpServer ready to receive requestes.");

		while (noStopRequest) {
			try {
				Socket socket = serverSocket.accept();

				if (fifoStackIdleWorkers.isEmpty()) {
					System.out.println("System is busy, denying service.");
					BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
					writer.write("HTTP/1.0 503 Service " + "Unavailable\r\n\r\n");
					writer.flush();
					writer.close();
					writer = null;
				} else {
					HttpWorker worker = (HttpWorker) fifoStackIdleWorkers.remove();
					worker.processRequest(socket);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	public void stopRequest() {
		noStopRequest = false;
		internalThread.interrupt();
		for (int i = 0; i < workerList.length; i++)
			workerList[i].stopRequest();
		if (null != serverSocket) {
			try {
				serverSocket.close();
			} catch (IOException ioex) {
			}
			serverSocket = null;
		}
	}

	public boolean isAlive() {
		return internalThread.isAlive();
	}

	private static void useageAndExit(String msg, int exitCode) {
		System.err.println(msg);
		System.err.println("Usage: java HttpServer <port> " + "<numOfWorkers> <documentRoot>");
		System.err.println("<port> port to listen on for HTTP request..");
		System.err.println("<numWorkers> number of worker threads to create.");
		System.err.println("<documentRoot> base directory for HTML files.");
		System.exit(exitCode);
	}
}