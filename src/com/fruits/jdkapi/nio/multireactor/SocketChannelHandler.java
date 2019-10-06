/*
 * Created on Jun 23, 2005
 *
 */
package com.fruits.jdkapi.nio.multireactor;

/**
 * @author TomHornson@hotmail.com
 * 
 */
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import com.fruits.jdkapi.nio.NIOConstants;

public class SocketChannelHandler implements Runnable {
	private ByteBuffer output = ByteBuffer.wrap("Handshake completed.".getBytes(NIOConstants.CHARSET));

	public static final int MAX_INPUT_BUFF = 1024;

	private final SocketChannel socketChannel;
	private final SelectionKey selectionkey;

	private ResizableByteBuffer input = new ResizableByteBuffer();
	private ByteBuffer inputBuffer = ByteBuffer.allocateDirect(MAX_INPUT_BUFF);

	public static final int READING = 0, SENDING = 1;
	private int state = READING;

	public SocketChannelHandler(Reactor reactor, SocketChannel socketChannel) throws IOException {
		this.socketChannel = socketChannel;
		socketChannel.configureBlocking(false);
		/*
		 * if selector(a Selector) is blocked on other thread because of select() and so on, then here will block. 
		 * It causes dead-lock.
		 */
		selectionkey = reactor.register(socketChannel, SelectionKey.OP_READ, this);
	}

	private boolean inputIsComplete() {
		ByteBuffer buff = input.getBuffer();
		int size = buff.position();
		if (size >= 2) {
			/*
			 * TODO: Is there something wrong?
			 */
			char c1 = (char) buff.get(size - 1);
			char c2 = (char) buff.get(size - 2);
			if (c1 == '\n' && c2 == '\r')
				return true;
		}
		return false;
	}

	private boolean outputIsComplete() {
		return true;
	}

	private void process() {
	}

	/*
	 * TODO: Apply ThreadPool.
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
			doEndingWork();
		} finally {
		}
	}

	void read() throws IOException {
		inputBuffer.clear();
		socketChannel.read(inputBuffer);
		input.put(inputBuffer);

		if (inputIsComplete()) {
			process();
			state = SENDING;
			// Normally also do first write now
			selectionkey.interestOps(SelectionKey.OP_WRITE);
		}
	}

	void send() throws IOException {
		/*
		 * TODO: May be ,there is a bug because of the dispersion of the buffs.
		 */
		socketChannel.write((ByteBuffer) input.getBuffer().flip());
		socketChannel.write(output);
		if (outputIsComplete()) {
			/*
			 * TODO: How to do ending work?
			 */
			doEndingWork();
		}
	}

	private void doEndingWork() {
		selectionkey.cancel();
		try {
			if ((null != socketChannel) && socketChannel.isOpen())
				socketChannel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
