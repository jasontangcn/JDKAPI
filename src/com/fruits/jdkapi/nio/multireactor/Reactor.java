/*
 * Created on 2005-9-21
 *
 */
package com.fruits.jdkapi.nio.multireactor;

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

import com.fruits.jdkapi.thread.threadpool.ThreadPoolManager;

public class Reactor extends Thread {
	private final Selector selector;
	private volatile boolean waiting = false;

	public Reactor() throws IOException {
		this.selector = Selector.open();
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
	
	private final static ThreadPoolManager threadPool;
	static {
			try {
				threadPool = new ThreadPoolManager(50);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
	}

	private void dispatch(SelectionKey key) {
		//It's an Acceptor or SocketChannelHandler
		Runnable handler = (Runnable) key.attachment();
		if (null != handler) {
			/**
			 * WARNING: 
			 * Do not run Acceptor/Handler on signal thread,
			 * because concurrent operations on Selector object will be blocked.
			 * 
			 * What about SocketChannelHandler?
			 * 
			 * Shouldn't we use ThreadPool in NIO Apps?
			 *
			 */
			try {
			    threadPool.excute(handler);
			}catch(InterruptedException e) {
			    e.printStackTrace();
			}
			//handler.run();
		}
	}

	public SelectionKey register(SelectableChannel selectableChannel, int ops) throws ClosedChannelException {
		return register(selectableChannel, ops, null);
	}

	public SelectionKey register(SelectableChannel selectableChannel, int ops, Object attachment) throws ClosedChannelException {
		setWaiting(true);
		selector.wakeup();
		SelectionKey selectionKey = selectableChannel.register(selector, ops, attachment);
		setWaiting(false);
		synchronized (this) {
			notifyAll();
		}
		return selectionKey;
	}
	
	private void setWaiting(boolean waiting) {
		this.waiting = waiting;
	}
}
