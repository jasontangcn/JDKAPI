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
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import com.fruits.jdkapi.thread.threadpool.ThreadPoolManager;

public class Reactor implements Runnable {
	final Selector selector;
	final ServerSocketChannel serverSocket;

	final static ThreadPoolManager threadPool;
	static {
		try {
			threadPool = new ThreadPoolManager(50);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	Reactor(int port) throws IOException {
		selector = Selector.open();
		serverSocket = ServerSocketChannel.open();
		InetSocketAddress address = new InetSocketAddress(InetAddress.getLocalHost(), port);
		serverSocket.socket().bind(address);

		serverSocket.configureBlocking(false);
		SelectionKey selectionKey = serverSocket.register(selector, SelectionKey.OP_ACCEPT);
		selectionKey.attach(new Acceptor());
	}

	public void run() { // usually in a new Thread
		try {
			while (!Thread.interrupted()) {
				if (selector.select() > 0) {
					Set selectedKeys = selector.selectedKeys();
					Iterator it = selectedKeys.iterator();
					while (it.hasNext())
						dispatch((SelectionKey) (it.next()));
					selectedKeys.clear();
				}
			}
		} catch (IOException e) {
		}
	}

	void dispatch(SelectionKey selectionKey) {
		Runnable acceptor = (Runnable) (selectionKey.attachment());
		if (null != acceptor) {
				try {
					threadPool.excute(acceptor);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		}
	}

	class Acceptor implements Runnable {
		public void run() {
			try {
				SocketChannel socketChannel = serverSocket.accept();
				if (null != socketChannel)
					new SocketIOHandler(selector, socketChannel);
			} catch (IOException e) {
			}
		}
	}

	public static void main(String[] args) throws InterruptedException, IOException {
		Reactor.threadPool.excute(new Reactor(8000));
	}
}
