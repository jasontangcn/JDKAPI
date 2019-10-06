/*
 * Created on 2005-9-16
 *
 */
package com.fruits.jdkapi.nio.multireactor;

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

import com.fruits.jdkapi.nio.NIOConstants;

public class Client {
	public static void main(String[] args) throws IOException {
		Charset charset = Charset.forName(NIOConstants.CHARSET);
		CharsetDecoder decoder = charset.newDecoder();

		ByteBuffer out = ByteBuffer.wrap("love.".getBytes());
		ByteBuffer in = ByteBuffer.allocateDirect(1024 * 3);
		SocketChannel serverSocket = null;
		try {
			serverSocket = SocketChannel.open(new InetSocketAddress(InetAddress.getLocalHost(), 8000));

			for (int i = 0; i < 500; i++) {
				serverSocket.write(out);
				out.flip();
			}
			try {
				Thread.sleep(1 * 1000);
			} catch (InterruptedException ie) {
				// ignore
			}
			serverSocket.write(out);
			// write terminated
			serverSocket.write(ByteBuffer.wrap("\r\n".getBytes()));

			serverSocket.read(in);
			in.flip();
			CharBuffer response = decoder.decode(in);
			System.out.println(response);
		} finally {
			try {
				if ((null != serverSocket) && serverSocket.isConnected()) {
					serverSocket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
