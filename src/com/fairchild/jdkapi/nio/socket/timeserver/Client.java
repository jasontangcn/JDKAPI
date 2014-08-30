package com.fairchild.jdkapi.nio.socket.timeserver;

import java.net.Socket;
import java.io.*;

public class Client {
	public Client() {
	}

	public static void main(String[] args) {

		try {
			Socket client = new Socket("localhost", 5100);
			client.setSoTimeout(10000);
			DataOutputStream os = new DataOutputStream((client.getOutputStream()));

			String query = "GB";
			byte[] request = query.getBytes();
			os.write(request);
			os.flush();
			client.shutdownOutput();

			DataInputStream is = new DataInputStream(client.getInputStream());
			byte[] reply = new byte[40];
			is.read(reply);
			System.out.println("Time: " + new String(reply, "GBK"));

			is.close();
			os.close();
			client.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
