package com.fairchild.jdkapi.nio.socket;

import java.util.List;
import java.util.LinkedList;
import java.nio.channels.SocketChannel;
import java.nio.channels.SelectionKey;

public final class Writer extends Thread {
    private static List selectedKeyPool = new LinkedList();
    private static Notifier notifier = Notifier.getNotifier();

    public Writer() {
    }
    
    public void run() {
        while (true) {
            try {
                SelectionKey key;
                synchronized (selectedKeyPool) {
                    while (selectedKeyPool.isEmpty()) {
                    	selectedKeyPool.wait();
                    }
                    key = (SelectionKey) selectedKeyPool.remove(0);
                }
                write(key);
            }
            catch (Exception e) {
                continue;
            }
        }
    }

    public void write(SelectionKey key) {
        try {
            SocketChannel sc = (SocketChannel) key.channel();
            Response response = new Response(sc);
            
            notifier.fireOnWrite((Request)key.attachment(), response);

            sc.finishConnect();
            sc.socket().close();
            sc.close();
            
            notifier.fireOnClosed((Request)key.attachment());
        }
        catch (Exception e) {
            notifier.fireOnError("Error occured in Writer: " + e.getMessage());
        }
    }

    public static void processRequest(SelectionKey key) {
        synchronized (selectedKeyPool) {
        	selectedKeyPool.add(selectedKeyPool.size(), key);
        	selectedKeyPool.notifyAll();
        }
    }
}
