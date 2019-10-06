package com.fairchild.jdkapi.serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializationUtil {

	public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
		ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(data));
		return objectInputStream.readObject();
	}

	public static byte[] serialize(Object object) throws IOException {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(bytes);
		objectOutputStream.writeObject(object);
		objectOutputStream.close();
		return bytes.toByteArray();
	}

	public static void log(Object s) {
		System.out.println(s);
	}

}
