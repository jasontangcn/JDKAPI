package com.fruits.jdkapi.socket.breakpoint;
import java.math.BigInteger;
import java.security.MessageDigest;

public class Utils {
	public static String getMD5(String str) throws Exception {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		byte[] digest = md5.digest(str.getBytes());
		return bytes2Hex(digest);
	}

	public static String bytes2Hex(byte[] bytes) {
		BigInteger i = new BigInteger(1, bytes);
		return i.toString(16);
	}

	public static void main(String[] args) throws Exception {
		System.out.println(getMD5("https://dldir1.qq.com/qqfile/qq/QQ9.0.5/23816/QQ9.0.5.exe"));

		byte i = -1;
		// 0000 0001
		// 1111 1110
		// 1111 1111
		int j = i & 0xff;
		System.out.println(i & 0xff);
	}
}
