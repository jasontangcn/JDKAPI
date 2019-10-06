package com.fairchild.jdkapi.serialization;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SerializableExternalizableMethods {
	public static class Base implements Serializable {
		private void writeObject(ObjectOutputStream out) throws IOException {
			SerializationUtil.log("Base.writeObject() start.");
			out.defaultWriteObject();
			SerializationUtil.log("Base.writeObject() end.");
		}

		private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
			SerializationUtil.log("Base.readObject() start.");
			in.defaultReadObject();
			SerializationUtil.log("Base.readObject() end.");
		}

		private Object writeReplace() {
			SerializationUtil.log("Base.writeReplace().");
			return this;
		}

		private Object readResolve() {
			SerializationUtil.log("Base.readResolve().");
			return this;
		}
	}

	public static class Child extends Base implements Serializable {
		private void writeObject(ObjectOutputStream out) throws IOException {
			SerializationUtil.log("Child.writeObject() start.");
			out.defaultWriteObject();
			SerializationUtil.log("Child.writeObject() end.");
		}

		private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
			SerializationUtil.log("Child.readObject() start.");
			in.defaultReadObject();
			SerializationUtil.log("Child.readObject() end.");
		}

		private Object writeReplace() {
			SerializationUtil.log("Child.writeReplace().");
			return this;
		}

		private Object readResolve() {
			SerializationUtil.log("Child.readResolve().");
			return this;
		}
	}

	public static void main(String[] args) throws Exception {
		byte[] data = SerializationUtil.serialize(new Child());
		Object obj = SerializationUtil.deserialize(data);
	}
}
