package com.fairchild.jdkapi.classloader.security;

import java.lang.reflect.Method;

public class CryptographicClassLoaderMain {
	//java CryptographicClassLoaderUtil keystore algorithm AppName arg0 arg1 arg2 ...
	public static void main(String[] args) throws Exception {
		String keystore = args[0];
		String algorithm = args[1];
		String appName = args[2];
		
		String appArgs[] = new String[args.length - 3];
		System.arraycopy(args, 3, appArgs, 0, args.length - 3 );

		CryptographicClassLoader classLoader = new CryptographicClassLoader(keystore, algorithm);
		Class c = classLoader.loadClass(appName, false);
		Class[] argTypes = {(new String[1]).getClass()};
		Method mainMethod = c.getMethod("main", argTypes);
		Object argsArray[] = {appArgs};
		//static method
		mainMethod.invoke(null, argsArray );
	}
}
