/*
 * Created on Jun 23, 2005
 *
 */
package com.fruits.jdkapi.nio.reactor;

/**
 * @author TomHornson@hotmail.com
 * 
 */
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import com.fruits.jdkapi.nio.NIOConstants;
import com.fruits.jdkapi.nio.multireactor.ResizableByteBuffer;

public class SocketIOHandler implements Runnable {
	private ByteBuffer output = ByteBuffer.wrap("Handshake completed.".getBytes(NIOConstants.CHARSET));

	public static final int MAX_INPUT_BUFF = 1024;

	private final SocketChannel socketChannel;
	private final SelectionKey selectionKey;

	private ResizableByteBuffer input = new ResizableByteBuffer();
	private ByteBuffer buf = ByteBuffer.allocateDirect(MAX_INPUT_BUFF);

	public static final int READING = 0, SENDING = 1;
	private int state = READING;

	public SocketIOHandler(Selector selector, SocketChannel socketChannel) throws IOException {
		this.socketChannel = socketChannel;
		socketChannel.configureBlocking(false);
		selectionKey = socketChannel.register(selector, 0);
		selectionKey.attach(this);
		selectionKey.interestOps(SelectionKey.OP_READ);
		selector.wakeup();
	}

	private boolean inputCompleted() {
		ByteBuffer buf = input.getBuffer();
		int size = buf.position();
		if (size >= 2) {
			// is there something wrong?
			char c1 = (char) buf.get(size - 1);
			char c2 = (char) buf.get(size - 2);
			if (c1 == '\n' && c2 == '\r')
				return true;
		}
		return false;
	}

	private boolean outputCompleted() {
		return true;
	}

	private void process() {
	}

	/*
	 * TODO: use ThreadPool.
	 */
	public void run() {
		try {
			if (state == READING) {
				read();
			} else if (state == SENDING) {
				send();
			}
		} catch (IOException e) {
			e.printStackTrace();
			close();
		} finally {
		}
	}

	void read() throws IOException {
		buf.clear();
		socketChannel.read(buf);
		input.put(buf);

		if (inputCompleted()) {
			process();
			state = SENDING;
			// usually also do first write now
			selectionKey.interestOps(SelectionKey.OP_WRITE);
		}
	}

	void send() throws IOException {
		// TODO: may be , there is a bug because of the dispersion of the buffers.
		socketChannel.write((ByteBuffer) input.getBuffer().flip());
		socketChannel.write(output);
		if (outputCompleted()) {
			// how to do ending work?
			close();
		}
	}

	private void close() {
		selectionKey.cancel();
		try {
			if ((null != socketChannel) && socketChannel.isOpen())
				socketChannel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
