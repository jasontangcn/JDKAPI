package com.fairchild.jdkapi.security;

import java.lang.reflect.Method;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;

public class ClassInternal implements SecurityMisc.InterfaceA, SecurityMisc.InterfaceB {	
	public static String getClassInfo(Class cls) {
		StringBuffer sb = new StringBuffer();
		
		ClassLoader cl = cls.getClassLoader();
		int count = 1;
		while (null != cl) {
			for(int i = 0; i < count; i++){
				sb.append("	");
			}
			sb.append(cl).append("\n");
			URL[] urls = getClassLoaderURLs(cl);
			if(null != urls) {
				for(URL url : urls){
					for(int i = 0; i < count; i++){
						sb.append("	");
					}
					sb.append(url).append("\n");
				}
			}
			count++;
			cl = cl.getParent();
		}
		
		sb.append("\n");
		
		ProtectionDomain pd = cls.getProtectionDomain();
		CodeSource cs = pd.getCodeSource();
		
		sb.append("ProtectionDomain is : ").append(pd);
		
		if (null != cs)
			sb.append("CodeSource is : ").append(cs).append("\n");
		else
			sb.append("CodeSource is NULL.");

		sb.append("Implemented interfaces:").append("\n");
		Class[] is = cls.getInterfaces();
		for (Class i : is) {
			sb.append("	" + i + "(" + Integer.toHexString(i.hashCode()) + ")");
			ClassLoader loader = i.getClassLoader();
			sb.append("	ClassLoader: " + loader);
			ProtectionDomain domain = i.getProtectionDomain();
			CodeSource source = domain.getCodeSource();
			if (null != source)
				sb.append("	CodeSource: " + source + "\n");
			else
				sb.append("	CodeSource is NULL.\n");
		}
		
		return sb.toString();
	}

	/**
	 * Use reflection to access a URL[] getURLs or ULR[] getAllURLs method so
	 * that non-URLClassLoader class loaders, or class loaders that override
	 * getURLs to return null or empty, can provide the true classpath info.
	 */
	public static URL[] getClassLoaderURLs(ClassLoader cl) {
		URL[] urls = {};
		try {
			Class returnType = urls.getClass();
			Class[] parameterTypes = {};
			Method getURLs = cl.getClass().getMethod("getURLs", parameterTypes);
			if (returnType.isAssignableFrom(getURLs.getReturnType())) {
				Object[] args = {};
				urls = (URL[]) getURLs.invoke(cl, args);
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		return urls;
	}

	public static void main(String[] args) {
		System.out.println("ClassInternal :");
		System.out.println(ClassInternal.getClassInfo(ClassInternal.class));
	}
}

