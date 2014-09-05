/*
 * Created on 2005-9-16
 *
 */
package com.fairchild.jdkapi.nio;

/**
 * @author TomHornson@hotmail.com
 *
 */
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class Client {
	public static void main(String[] args) {
		Charset charset = Charset.forName("UTF-8");
		CharsetDecoder decoder = charset.newDecoder();
		ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
		SocketChannel socketChannel = null;
		try {
			socketChannel = SocketChannel.open(new InetSocketAddress(InetAddress.getLocalHost(), 8000));
			buffer.clear();
			socketChannel.read(buffer);
			buffer.flip();
			CharBuffer charBuffer = decoder.decode(buffer);
			System.out.println(charBuffer);
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
}
