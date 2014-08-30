package com.fairchild.jdkapi.nio.socket;

import java.nio.channels.SocketChannel;

public class Request {
    private SocketChannel sc;
    private byte[] data = null;;
    private Object attachment = null;
    
    public Request(SocketChannel sc) {
        this.sc = sc;
    }
    
    public java.net.InetAddress getAddress() {
        return sc.socket().getInetAddress();
    }
    
    public int getPort() {
        return sc.socket().getPort();
    }
    
    public boolean isConnected() {
        return sc.isConnected();
    }
    
    public boolean isBlocking() {
        return sc.isBlocking();
    }
    
    public boolean isConnectionPending() {
        return sc.isConnectionPending();
    }
    
    public boolean getKeepAlive() throws java.net.SocketException {
        return sc.socket().getKeepAlive();
    }
    
    public int getSoTimeout() throws java.net.SocketException {
        return sc.socket().getSoTimeout();
    }
    
    public boolean getTcpNoDelay() throws java.net.SocketException {
        return sc.socket().getTcpNoDelay();
    }
    
    public boolean isClosed() {
        return sc.socket().isClosed();
    }
    
    public void attach(Object attachment) {
        this.attachment = attachment;
    }
    
    public Object attachment() {
        return this.attachment;
    }
    
    public byte[] getData() {
        return data;
    }
    
    public void setData(byte[] data) {
        this.data = data;
    }
}
