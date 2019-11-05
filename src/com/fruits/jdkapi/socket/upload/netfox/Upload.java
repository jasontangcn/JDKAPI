package com.fruits.jdkapi.socket.upload.netfox;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(asyncSupported = true, urlPatterns = { "/Upload" })
public class Upload extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String TEMP_FILE_NAME = "temp.data";
	public static final String TEMP_FILE_DIR = "D:\\Files";

	Random random = new Random(1024);

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String contentType = request.getContentType();
		String boundary = "--" + contentType.substring(contentType.indexOf("boundary=") + 9);

		ServletInputStream sis = request.getInputStream();
		String fileName = TEMP_FILE_DIR + "\\" + this.TEMP_FILE_NAME;
		File tempFile = new File(fileName);
		FileOutputStream os = new FileOutputStream(tempFile);
		byte[] buffer = new byte[5];
		int n = 0;
		while (-1 != (n = sis.read(buffer))) {
			os.write(buffer, 0, n);
		}
		os.close();
		sis.close();

		Input[] inputs = this.parseFormData(fileName, boundary.getBytes());
		System.out.println(inputs.length);

		for (Input input : inputs) {
			String fname = null;
			if (null != input.getFileName())
				fname = input.getFileName();
			else
				fname = input.getName();

			File file = new File(TEMP_FILE_DIR + "\\" + fname);
			System.out.println(file + "created");

			FileOutputStream fos = new FileOutputStream(file);
			fos.write(input.getData());
			fos.flush();
			fos.close();
		}
	}

	private Input[] parseFormData(String tempDataFile, byte[] boundary) throws IOException {
		RandomAccessFile raf = new RandomAccessFile(tempDataFile, "rw");
		byte[] tempData = new byte[(int) raf.length()];
		raf.readFully(tempData);
		raf.close();
		int start = 0, end = tempData.length;

		ArrayList<Input> files = new ArrayList<Input>();

		for (;;) {
			int i = indexOf(tempData, start, end, boundary);
			if (i == start) {
				int j = indexOf(tempData, start + 2, end, boundary);
				if (-1 != j) {
					Input file = parseFile(tempData, start + boundary.length + 2, (j - 2));
					files.add(file);
					start = j;
					continue;
				}
				break;
			}
			break;
		}

		return files.toArray(new Input[0]);
	}

	private Input parseFile(byte[] tempData, int start, int end) {
		Input file = new Input();

		int i = indexOf(tempData, start, end, "\n".getBytes());
		int j = indexOf(tempData, i + 1, end, "\n".getBytes());

		int k;
		if (j == (i + 2)) {
			k = (j + 1);

			int nameStart = indexOf(tempData, start, i - 1, "name=\"".getBytes());
			int nameEnd = indexOf(tempData, nameStart + 6, i - 1, "\"".getBytes());
			String name = new String(Arrays.copyOfRange(tempData, nameStart + 6, nameEnd));
			file.setName(name);

		} else {
			k = (j + 3);
			int nameStart = indexOf(tempData, start, i - 1, "name=\"".getBytes());
			int nameEnd = indexOf(tempData, nameStart, i - 1, "\";".getBytes());
			String name = new String(Arrays.copyOfRange(tempData, nameStart + 6, nameEnd));

			int fileNameStart = indexOf(tempData, start, i - 1, "filename=\"".getBytes());
			int fileNameEnd = indexOf(tempData, fileNameStart + 10, i - 1, "\"".getBytes());
			String fileName = new String(Arrays.copyOfRange(tempData, fileNameStart + 10, fileNameEnd));
			int slash = fileName.lastIndexOf('\\');
			if (-1 != slash) {
				fileName = fileName.substring(slash + 1, fileName.length());
			}
			file.setName(name);
			file.setFileName(fileName);
		}

		byte[] data = Arrays.copyOfRange(tempData, k, end);

		file.setData(data);

		String fileName = String.valueOf(random.nextInt());
		//System.out.println("fileName: " + fileName);
		//file.setName(fileName);
		return file;
	}

	/**
	   * Search the data byte array for the first occurrence of the byte array pattern within given boundaries.
	   * @param data
	   * @param start First index in data
	   * @param stop Last index in data so that stop-start = length
	   * @param pattern What is being searched. '*' can be used as wildcard for "ANY character"
	   * @return
	   */
	public static int indexOf(byte[] data, int start, int stop, byte[] pattern) {
		int[] failure = computeFailure(pattern);

		int j = 0;

		for (int i = start; i < stop; i++) {
			while (j > 0 && (pattern[j] != '*' && pattern[j] != data[i])) {
				j = failure[j - 1];
			}
			if (pattern[j] == '*' || pattern[j] == data[i]) {
				j++;
			}
			if (j == pattern.length) {
				return i - pattern.length + 1;
			}
		}
		return -1;
	}

	/**
	 * Computes the failure function using a boot-strapping process,
	 * where the pattern is matched against itself.
	 */
	private static int[] computeFailure(byte[] pattern) {
		int[] failure = new int[pattern.length];

		int j = 0;
		for (int i = 1; i < pattern.length; i++) {
			while (j > 0 && pattern[j] != pattern[i]) {
				j = failure[j - 1];
			}
			if (pattern[j] == pattern[i]) {
				j++;
			}
			failure[i] = j;
		}

		return failure;
	}

	private class Input {
		private String name;
		private String fileName;
		private byte[] data;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public byte[] getData() {
			return data;
		}

		public void setData(byte[] data) {
			this.data = data;
		}
	}
}
