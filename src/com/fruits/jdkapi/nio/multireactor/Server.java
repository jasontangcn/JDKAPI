/*
 * Created on 2005-9-19
 *
 */
package com.fruits.jdkapi.nio.multireactor;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

/**
 * @author TomHornson@hotmail.com
 *
 */
public class Server {
	public Server(InetAddress address, int port) throws IOException {
		Reactor mainReactor = new Reactor();
		// run it on a signal thread,
		// if run it on main thread, this program will block because of the while clause.
		mainReactor.start();
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.configureBlocking(false);
		serverSocketChannel.socket().bind(new InetSocketAddress(address, port), 50);

		new Acceptor(mainReactor, serverSocketChannel, 10);
	}

	public Server(byte[] address, int port) throws IOException {
		this(InetAddress.getByAddress(address), port);
	}

	public Server(String host, int port) throws IOException {
		this(InetAddress.getByName(host), port);
	}

	public static void main(String[] args) throws IOException {
		InetAddress address = InetAddress.getLocalHost();
		/**
		 * if address is null, system will pick up a invalid local address to bind.
		 * if port is 0, system will pick up an ephenmeral(random) port to bind.
		 */
		Server server = new Server(address, 8000);
	}
}
