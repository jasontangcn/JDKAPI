/*
 * Created on Jun 23, 2005
 *
 */
package com.fairchild.jdkapi.nio.reactor;

/**
 * @author TomHornson@hotmail.com
 * 
 */
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import com.fairchild.jdkapi.nio.NIOConstants;
import com.fairchild.jdkapi.nio.multireactor.ResizableByteBuffer;

public class SocketReadHandler implements Runnable {
	private ByteBuffer output = ByteBuffer.wrap("Handshake completed.".getBytes(NIOConstants.CHARSET));

	public static final int MAX_INPUT_BUFF = 1024;

	private final SocketChannel socketChannel;
	private final SelectionKey selectionKey;

	private ResizableByteBuffer input = new ResizableByteBuffer();
	private ByteBuffer inputBuff = ByteBuffer.allocateDirect(MAX_INPUT_BUFF);

	public static final int READING = 0, SENDING = 1;
	private int state = READING;

	public SocketReadHandler(Selector selector, SocketChannel socketChannel) throws IOException {
		this.socketChannel = socketChannel;
		socketChannel.configureBlocking(false);
		selectionKey = socketChannel.register(selector, 0);
		// 将SelectionKey绑定为本Handler下一步有事件触发时，将调用本类的run方法
		// 参看dispatch(SelectionKey selectionKey)
		selectionKey.attach(this);
		// 同时将SelectionKey标记为可读，以便读取
		selectionKey.interestOps(SelectionKey.OP_READ);
		selector.wakeup();
	}

	private boolean inputIsComplete() {
		ByteBuffer buff = input.buffer();
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
		inputBuff.clear();
		socketChannel.read(inputBuff);
		input.put(inputBuff);

		if (inputIsComplete()) {
			process();
			state = SENDING;
			// Normally also do first write now
			selectionKey.interestOps(SelectionKey.OP_WRITE);
		}
	}

	void send() throws IOException {
		/*
		 * TODO: may be ,there is a bug because of the dispersion of the buffs.
		 */
		socketChannel.write((ByteBuffer) input.buffer().flip());
		socketChannel.write(output);
		if (outputIsComplete()) {
			/*
			 * TODO: How to do ending work?
			 */
			doEndingWork();
		}
	}

	private void doEndingWork() {
		selectionKey.cancel();
		try {
			if ((null != socketChannel) && socketChannel.isOpen())
				socketChannel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
