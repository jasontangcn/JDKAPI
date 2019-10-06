package com.fairchild.jdkapi.classloader.hotdeploy;


/**
 CustomClassLoader.java
 This is a CustomClassLoader implemented according to the Java 1.2 parent delegation model.
 It extends java.lang.Classloader & overwrite the findClass method. 
 CustomClassLoader searches classes in the specified path.
 This implementation can not find classes in jar/zip files, the specified path should be a list of directories.
 */

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;

public class CustomClassLoader extends ClassLoader {
	private List<String> classRepository = new ArrayList<String>(); //where to find the classes

	public CustomClassLoader(ClassLoader parent, String classPath) {
		super(parent);
		initLoader(classPath);
	}

	public CustomClassLoader(String classPath) {
		super(CustomClassLoader.class.getClassLoader());
		initLoader(classPath);
	}

	/**
	 * This method overrides the findClass method in the java.lang.ClassLoader Class.
	 * The method will be called from the loadClass method of the parent
	 * class loader when it is unable to find a class to load. 
	 * This implementation will look for the class in the class repository.
	 * 
	 * @param className
	 * 			the class to be loaded
	 * @return	A Class object which is loaded into the JVM by the CustomClassLoader
	 * @throws ClassNotFoundException
	 * 			If the method is unable to find the class.
	 */
	protected Class findClass(String className) throws ClassNotFoundException {
		System.out.println("ClassLoader->findClass...");
		byte[] bytes = loadFromCustomRepository(className);
		if (null != bytes) {
			return defineClass(className, bytes, 0, bytes.length);
		}
		throw new ClassNotFoundException(className);
	}

	/*
	 * A private method that loads binary class file data from the classRepository.
	 */
	private byte[] loadFromCustomRepository(String classFileName) throws ClassNotFoundException {
		Iterator<String> dirs = classRepository.iterator();
		byte[] classBytes = null;
		while (dirs.hasNext()) {
			String dir = (String) dirs.next();
			// Replace '.' in the class name with File.separatorChar & append .class to the name.
			classFileName.replace('.', File.separatorChar);
			classFileName += ".class";
			try {
				File file = new File(dir, classFileName);
				if (file.exists()) {
					// read file
					InputStream is = new FileInputStream(file);
					classBytes = new byte[is.available()];
					is.read(classBytes);
					is.close();
					break;
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return classBytes;
	}

	private void initLoader(String classPath) {
		// classPath is a string of directories/jar files separated by the File.pathSeparator
		if ((null != classPath) && !("".equals(classPath))) {
			StringTokenizer tokenizer = new StringTokenizer(classPath, File.pathSeparator);
			while (tokenizer.hasMoreTokens()) {
				classRepository.add(tokenizer.nextToken());
			}
		}

	}
}
