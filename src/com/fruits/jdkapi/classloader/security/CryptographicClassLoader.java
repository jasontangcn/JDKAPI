package com.fruits.jdkapi.classloader.security;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import com.fruits.jdkapi.io.FileUtil;

public class CryptographicClassLoader extends ClassLoader {
	private Cipher cipher = null;

	public CryptographicClassLoader(String keystore, String alogrithm) throws Exception {
		SecureRandom sr = new SecureRandom();
		DESKeySpec keySpec = new DESKeySpec(FileUtil.readFile(keystore));
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(alogrithm);
		SecretKey secretKey = keyFactory.generateSecret(keySpec);
		cipher = Cipher.getInstance(alogrithm);
		cipher.init(Cipher.DECRYPT_MODE, secretKey, sr);
	}

	@Override
	protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
		Class c = this.findLoadedClass(name);
		if (null != c)
			return c;
		byte[] classData = null;
		try {
			classData = cipher.doFinal(FileUtil.readFile(name + ".class.encrypted"));
		} catch (Exception ioe) {
			//ioe.printStackTrace();
		}

		if ((null != classData) && (0 < classData.length)) {
			c = this.defineClass(name, classData, 0, classData.length);
		}

		if (null == c) {
			c = this.findSystemClass(name);
		}

		if ((null != c) && resolve)
			this.resolveClass(c);

		return c;
	}

	static byte[] encrypt(byte[] data, byte[] rawKey, String alogrithm) throws Exception {
		SecureRandom sr = new SecureRandom();
		DESKeySpec keySpec = new DESKeySpec(rawKey);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(alogrithm);
		SecretKey secretKey = keyFactory.generateSecret(keySpec);
		Cipher cipher = Cipher.getInstance(alogrithm);

		cipher.init(Cipher.ENCRYPT_MODE, secretKey, sr);
		return cipher.doFinal(data);
	}

	static byte[] decrypt(byte[] encryptedData, byte[] rawKey, String alogrithm) throws Exception {
		SecureRandom sr = new SecureRandom();
		DESKeySpec keySpec = new DESKeySpec(rawKey);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(alogrithm);
		SecretKey secretKey = keyFactory.generateSecret(keySpec);
		Cipher cipher = Cipher.getInstance(alogrithm);
		cipher.init(Cipher.DECRYPT_MODE, secretKey, sr);
		return cipher.doFinal(encryptedData);
	}

	//alogrithm : DES
	static byte[] generateKey(String alogrithm) throws Exception {
		SecureRandom sr = new SecureRandom();
		KeyGenerator kg = KeyGenerator.getInstance(alogrithm);
		kg.init(sr);
		SecretKey key = kg.generateKey();
		return key.getEncoded();
	}

	public static final String KEY_STORE_NAME = "keystore";

	private static String getFileName(String path) {
		int i = path.lastIndexOf('\\');
		if (-1 == i) {
			return path;
		} else {
			return path.substring(i + 1);
		}
	}
}
