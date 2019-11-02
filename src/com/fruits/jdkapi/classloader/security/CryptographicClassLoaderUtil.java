package com.fruits.jdkapi.classloader.security;

import com.fruits.jdkapi.io.FileUtil;

public class CryptographicClassLoaderUtil {

	public static void main(String[] args) throws Exception {
		if(args.length > 0){
			String command = args[0];
			if("generateKey".equals(command)) {
				if(args.length > 1) {
				  String algorithm = args[1];
				  FileUtil.writeFile(CryptographicClassLoader.generateKey("DES"), "keystore");
				}
			}else if("encrypt".equals(command)){
				if(args.length > 3) {
					String algorithm = args[1];
					String keystore = args[2];
					byte[] rawKey = FileUtil.readFile(keystore);
					for(int i = 3; i < args.length; i++) {
						String filePath = args[i];
						byte[] rawData = FileUtil.readFile(filePath);
						FileUtil.writeFile(CryptographicClassLoader.encrypt(rawData,rawKey,algorithm), filePath + ".encryted");
					}
				}
			}else if("decrypt".equals(command)){
				if(args.length > 3) {
					String algorithm = args[1];
					String keystore = args[2];
					byte[] rawKey = FileUtil.readFile(keystore);
					for(int i = 3; i < args.length; i++) {
						String filePath = args[i];
						byte[] rawData = FileUtil.readFile(filePath);
						FileUtil.writeFile(CryptographicClassLoader.decrypt(rawData,rawKey,algorithm), filePath + ".decryted");
					}
				}
			}
		}
	}

}
