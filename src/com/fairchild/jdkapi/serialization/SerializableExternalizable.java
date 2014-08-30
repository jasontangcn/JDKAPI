package com.fairchild.jdkapi.serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SerializableExternalizable {
	public static class Base implements Serializable,Externalizable {
		public void writeExternal(ObjectOutput out) throws IOException {
			SerializationUtil.log("writeExternal() start.");
			SerializationUtil.log("writeExternal() end.");
		}

		public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
			SerializationUtil.log("readExternal() start.");
			SerializationUtil.log("readExternal() end.");
		}

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

	public static void main(String[] args) throws Exception {
		byte[] bytes = SerializationUtil.serialize(new Base());
		SerializationUtil.deserialize(bytes);
	}

}
