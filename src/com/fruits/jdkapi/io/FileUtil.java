package com.fairchild.jdkapi.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class FileUtil {
	public static void writeFile(byte[] data, String filePath) throws Exception {
		FileOutputStream fos = new FileOutputStream(new File(filePath));
		fos.write(data);
		fos.close();
	}
	
	public static byte[] readFile(String filePath) throws Exception {
		FileInputStream fis = new FileInputStream(new File(filePath));
		byte[] bytes = new byte[fis.available()];
		fis.read(bytes);
		fis.close();
		return bytes;
	}
}
