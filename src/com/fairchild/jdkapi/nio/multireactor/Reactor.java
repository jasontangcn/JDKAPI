/*
 * Created on 2005-9-21
 *
 */
package com.fairchild.jdkapi.nio.multireactor;

/**
 * @author TomHornson@hotmail.com
 *
 */
import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

public class Reactor extends Thread {
	private final Selector selector;
	private volatile boolean waiting = false;

	public Reactor() throws IOException {
		this.selector = Selector.open();
	}

	private void waiting(boolean waiting) {
		this.waiting = waiting;
	}

	public void run() {
		try {
			while (!Thread.interrupted()) {
				Printer.debug("[" + Thread.currentThread().getName() + "] " + " is selecting.");
				if (selector.select() > 0) {
					Printer.debug("[" + Thread.currentThread().getName() + "] " + " got selected keys.");
					Set selectedKeys = selector.selectedKeys();
					for (Iterator it = selectedKeys.iterator(); it.hasNext();)
						dispatch((SelectionKey) (it.next()));
					selectedKeys.clear();
				}

				if (waiting) {
					try {
						synchronized (this) {
							wait();
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			/*
			 * TODO: How about the SlectionKey of ServerSocketChannel.
			 */
			try {
				if ((null != selector) && selector.isOpen())
					selector.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void dispatch(SelectionKey key) {
		//It's an Acceptor or SocketChannelHandler
		Runnable handler = (Runnable) key.attachment();
		if (null != handler) {
			/**
			 * WARNING: Running Acceptor/Handler on signal thread is wrong.
			 * Concurrent operations on Selector object are blocked.
			 * 
			 * What about SocketChannelHandler?
			 * 
			 * Shouldn't we use ThreadPool in NIO Apps?
			 *
			 * 
			 */
			handler.run();
		}
	}

	public SelectionKey register(SelectableChannel selectableChannel, int ops) throws ClosedChannelException {
		return register(selectableChannel, ops, null);
	}

	public SelectionKey register(SelectableChannel selectableChannel, int ops, Object attachment) throws ClosedChannelException {
		waiting(true);
		selector.wakeup();
		SelectionKey selectionKey = selectableChannel.register(selector, ops, attachment);
		waiting(false);
		synchronized (this) {
			notifyAll();
		}
		return selectionKey;
	}
}
