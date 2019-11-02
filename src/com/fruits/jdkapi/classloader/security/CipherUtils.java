package com.fruits.jdkapi.classloader.security;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.*;

public class CipherUtils {
	Cipher createCipher(String password, int mode) throws Exception {
		char[] pwd = password.toCharArray();
		PBEKeySpec keyspec = new PBEKeySpec(pwd);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
		SecretKey key = keyFactory.generateSecret(keyspec);
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(password.getBytes());
		byte[] digest = md.digest();
		byte[] salt = new byte[8];
		for (int i = 0; i < 8; i++)
			salt[i] = digest[i];
		PBEParameterSpec spec = new PBEParameterSpec(salt, 20);
		Cipher c = Cipher.getInstance("PBEWithMD5AndDES");
		c.init(mode, key, spec);
		return c;
	}
}
