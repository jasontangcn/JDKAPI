package com.fairchild.jdkapi.nio.socket;

import java.util.List;
import java.util.LinkedList;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.Selector;
import java.nio.channels.SelectionKey;
import java.util.Iterator;
import java.util.Set;

public class Server implements Runnable {
	public static final int MAX_THREADS = 4;

	private int port;
	private Notifier notifier;

	private static Selector selector;
	private ServerSocketChannel serverSocketChannel;
	private InetSocketAddress address;

	private static List writeRequestPool = new LinkedList();

	public Server(int port) throws Exception {
		this.port = port;
		notifier = Notifier.getNotifier();
		for (int i = 0; i < MAX_THREADS; i++) {
			Thread reader = new Reader();
			Thread writer = new Writer();
			reader.start();
			writer.start();
		}

		selector = Selector.open();
		serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.configureBlocking(false);
		ServerSocket socket = serverSocketChannel.socket();
		socket.bind(new InetSocketAddress(port));
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
	}

	public void run() {
		System.out.println("Server started and is listening on port: " + port);
		while (true) {
			try {
				int num = selector.select();

				if (num > 0) {
					Set selectedKeys = selector.selectedKeys();
					Iterator it = selectedKeys.iterator();
					while (it.hasNext()) {
						SelectionKey key = (SelectionKey) it.next();
						it.remove();
						if (SelectionKey.OP_ACCEPT == (key.readyOps() & SelectionKey.OP_ACCEPT)) {
							ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
							notifier.fireOnAccept();

							SocketChannel sc = ssc.accept();
							sc.configureBlocking(false);

							Request request = new Request(sc);
							notifier.fireOnAccepted(request);

							sc.register(selector, SelectionKey.OP_READ, request);
						} else if (SelectionKey.OP_READ == (key.readyOps() & SelectionKey.OP_READ)) {
							Reader.processRequest(key);
							key.cancel();
						} else if ((key.readyOps() & SelectionKey.OP_WRITE) == SelectionKey.OP_WRITE) {
							Writer.processRequest(key);
							key.cancel();
						}
					}
				} else {
					addRegister();
				}
			} catch (Exception e) {
				notifier.fireOnError("Error occured in Server: " + e.getMessage());
				continue;
			}
		}
	}

	private void addRegister() {
		synchronized (writeRequestPool) {
			while (!writeRequestPool.isEmpty()) {
				SelectionKey key = (SelectionKey) writeRequestPool.remove(0);
				SocketChannel schannel = (SocketChannel) key.channel();
				try {
					schannel.register(selector, SelectionKey.OP_WRITE, key.attachment());
				} catch (Exception e) {
					try {
						schannel.finishConnect();
						schannel.close();
						schannel.socket().close();
						notifier.fireOnClosed((Request) key.attachment());
					} catch (Exception exc) {
					}
					notifier.fireOnError("Error occured in addRegister: " + e.getMessage());
				}
			}
		}
	}

	public static void processWriteRequest(SelectionKey key) {
		synchronized (writeRequestPool) {
			writeRequestPool.add(writeRequestPool.size(), key);
			writeRequestPool.notifyAll();
		}
		selector.wakeup();
	}
}
