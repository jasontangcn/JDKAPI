/*
 * Created on 2005-9-16
 *
 */
package com.fairchild.jdkapi.nio;

/**
 * @author TomHornson@hotmail.com
 */
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {
	private static ByteBuffer content = ByteBuffer.wrap("NIOServer".getBytes());
	private InetAddress address;
	private int port;

	public NIOServer(InetAddress address, int port) {
		this.address = address;
		this.port = port;
	}

	public NIOServer(byte[] address, int port) throws UnknownHostException {
		this.address = InetAddress.getByAddress(address);
		this.port = port;
	}

	public NIOServer(String host, int port) throws UnknownHostException {
		this.address = InetAddress.getByName(host);
		this.port = port;
	}

	public void startServe() {
		Selector selector = null;
		ServerSocketChannel serverSocketChannel = null;
		try {
			selector = Selector.open();
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(false);
			// default backlog
			serverSocketChannel.socket().bind(new InetSocketAddress(address, port));

			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
			while (selector.select() > 0) {
				Set selectedKeys = selector.selectedKeys();
				Iterator iter = selectedKeys.iterator();
				while (iter.hasNext()) {
					SelectionKey sk = (SelectionKey) iter.next();
					iter.remove();
					processSelectionKey(sk);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (serverSocketChannel != null && serverSocketChannel.isOpen()) {
					serverSocketChannel.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				if ((null != selector) && selector.isOpen()) {
					selector.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void processSelectionKey(SelectionKey selectedKey) {
		ServerSocketChannel serverSocketChannel = null;
		SocketChannel socketChannel = null;
		try {
			if (selectedKey.isValid()) {
				serverSocketChannel = (ServerSocketChannel) selectedKey.channel();
				socketChannel = serverSocketChannel.accept();
				socketChannel.write(content);
				content.flip();
				System.out.println("dealed with a connection.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if ((null != socketChannel) && socketChannel.isConnected()) {
					socketChannel.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws Exception {
		int port = 8000;
		NIOServer svr = new NIOServer(InetAddress.getLocalHost(), port);
		svr.startServe();
	}
}
