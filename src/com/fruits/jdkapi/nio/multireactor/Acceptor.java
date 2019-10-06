/*
 * Created on 2005-9-21
 *
 */
package com.fruits.jdkapi.nio.multireactor;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Acceptor implements Runnable {
	private final Reactor mainReactor;
	private final Reactor[] subReactors;
	private final int reactorCapacity;
	private int index = 0;

	private final ServerSocketChannel serverSocketChannel;

	public Acceptor(Reactor mainReactor, ServerSocketChannel serverSocketChannel, int reactorCapacity) throws IOException {
		this.mainReactor = mainReactor;
		this.serverSocketChannel = serverSocketChannel;
		this.reactorCapacity = reactorCapacity;

		mainReactor.register(serverSocketChannel, SelectionKey.OP_ACCEPT, this);
		subReactors = new Reactor[reactorCapacity];
		for (int i = 0; i < reactorCapacity; i++) {
			Reactor subReactor = new Reactor();
			subReactors[i] = subReactor;
			subReactor.start();
		}
	}

	public void run() {
		/**
		 * Round-Robin Strategy
		 */
		Reactor reactor = null;
		if (reactorCapacity > 0) {
			reactor = subReactors[index];
			index = (index + 1) % reactorCapacity;
		}
		try {
			SocketChannel serverSocket = serverSocketChannel.accept();
			if (null != serverSocket) {
				if (null == reactor)
					reactor = mainReactor;
				new SocketChannelHandler(reactor, serverSocket);

			}
		} catch (IOException e) {
			e.printStackTrace();
			/*
			try {
				if ((null != serverSocket) && serverSocket.isOpen())
					serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			*/
		}
	}
}