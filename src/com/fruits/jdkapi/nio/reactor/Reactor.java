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
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import com.fairchild.jdkapi.thread.threadpool.ThreadPoolManager;

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
		// 向selector注册该channel
		SelectionKey selectionKey = serverSocket.register(selector, SelectionKey.OP_ACCEPT);
		// 利用selectionKey的attache功能绑定Acceptor如果有事情，触发Acceptor
		selectionKey.attach(new Acceptor());
	}

	public void run() { // normally in a new Thread
		try {
			while (!Thread.interrupted()) {
				if (selector.select() > 0) {
					Set selectedKeys = selector.selectedKeys();
					Iterator it = selectedKeys.iterator();
					// Selector如果发现channel有OP_ACCEPT或READ事件发生，下列遍历就会进行。
					while (it.hasNext())
						// 触发一个accepter线程
						// 以后触发SocketReadHandler
						dispatch((SelectionKey) (it.next()));
					selectedKeys.clear();
				}
			}
		} catch (IOException e) {
		}
	}

	// 运行Acceptor或SocketReadHandler
	void dispatch(SelectionKey selectionKey) {
		Runnable acceptor = (Runnable) (selectionKey.attachment());
		if (null != acceptor) {
			if (null != threadPool) {
				try {
					threadPool.excute(acceptor);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				acceptor.run();
			}
		}
	}

	class Acceptor implements Runnable { // inner
		public void run() {
			try {
				SocketChannel socketChannel = serverSocket.accept();
				if (null != socketChannel)
					// 调用Handler来处理channel
					new SocketReadHandler(selector, socketChannel);
			} catch (IOException e) {
			}
		}
	}

	public static void main(String[] args) throws InterruptedException, IOException {
		Reactor.threadPool.excute(new Reactor(8000));
	}
}
