/*
 * Created on 2005-9-21
 *
 */
package com.fruits.jdkapi.nio.multireactor;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Main reactor is responsible for event OP_ACCEPT and sub reactor is responsible for OP_READ & OP_WRITE.
 * 
 * @author TomHornson@hotmail.com
 *
 */
public class Acceptor implements Runnable {
	private final Reactor mainReactor;
	private final Reactor[] subReactors;
	private int index = 0;

	private final ServerSocketChannel serverSocketChannel;

	public Acceptor(Reactor mainReactor, ServerSocketChannel serverSocketChannel, int subReactorSize) throws IOException {
		this.mainReactor = mainReactor;
		this.serverSocketChannel = serverSocketChannel;

		mainReactor.register(serverSocketChannel, SelectionKey.OP_ACCEPT, this);
		
		subReactors = new Reactor[subReactorSize];
		for (int i = 0; i < subReactorSize; i++) {
			Reactor subReactor = new Reactor();
			subReactors[i] = subReactor;
			subReactor.start();
		}
	}

	public void run() {
    // round-robin strategy
		Reactor reactor = null;
		if (index > 0) {
			reactor = subReactors[index];
			index = (index + 1) % index;
		}
		
		try {
			SocketChannel serverSocket = serverSocketChannel.accept(); // blocks till there is connection accepted.
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