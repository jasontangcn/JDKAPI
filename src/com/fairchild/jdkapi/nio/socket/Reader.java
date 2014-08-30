package com.fairchild.jdkapi.nio.socket;

import java.util.List;
import java.util.LinkedList;
import java.nio.channels.SocketChannel;
import java.nio.channels.SelectionKey;
import java.util.Date;
import java.nio.ByteBuffer;
import java.io.IOException;

public class Reader extends Thread {
	private static List selectedKeyPool = new LinkedList();
	private static Notifier notifier = Notifier.getNotifier();

	public Reader() {
	}

	public void run() {
		while (true) {
			try {
				SelectionKey key;
				synchronized (selectedKeyPool) {
					while (selectedKeyPool.isEmpty()) {
						selectedKeyPool.wait();
					}
					key = (SelectionKey) selectedKeyPool.remove(0);
				}
				read(key);
			} catch (Exception e) {
				continue;
			}
		}
	}

	private static int BUFFER_SIZE = 1024;

	public static byte[] readRequest(SocketChannel sc) throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
		int off = 0;
		int num = 0;
		byte[] data = new byte[BUFFER_SIZE * 10];

		while (true) {
			buffer.clear();
			num = sc.read(buffer);
			if (-1 == num)
				break;
			if ((off + num) > data.length) {
				data = grow(data, BUFFER_SIZE * 10);
			}
			byte[] buf = buffer.array();
			System.arraycopy(buf, 0, data, off, num);
			off += num;
		}
		byte[] req = new byte[off];
		System.arraycopy(data, 0, req, 0, off);
		return req;
	}

	public void read(SelectionKey key) {
		try {
			SocketChannel sc = (SocketChannel) key.channel();
			byte[] data = readRequest(sc);

			Request request = (Request) key.attachment();
			request.setData(data);

			notifier.fireOnRead(request);

			Server.processWriteRequest(key);
		} catch (Exception e) {
			notifier.fireOnError("Error occured in Reader: " + e.getMessage());
		}
	}

	public static void processRequest(SelectionKey key) {
		synchronized (selectedKeyPool) {
			selectedKeyPool.add(selectedKeyPool.size(), key);
			selectedKeyPool.notifyAll();
		}
	}

	public static byte[] grow(byte[] src, int size) {
		byte[] tmp = new byte[src.length + size];
		System.arraycopy(src, 0, tmp, 0, src.length);
		return tmp;
	}
}
