/*
 * Created on 2005-9-19
 *
 */
package com.fruits.jdkapi.nio.multireactor;

/**
 * @author TomHornson@hotmail.com
 *
 */
import java.nio.ByteBuffer;

public class ResizableByteBuffer {
	public static final int DEFAULT_CAPACITY = 1024;
	private ByteBuffer data;

	public ResizableByteBuffer() {
		this(DEFAULT_CAPACITY);
	}

	public ResizableByteBuffer(int initialCapacity) {
		data = ByteBuffer.allocateDirect(initialCapacity);
	}

	public ResizableByteBuffer put(ByteBuffer buff) {
		int currentSize = data.position();
		int bufferSize = buff.position();
		int totalSize = currentSize + bufferSize;
		if (totalSize > data.capacity()) {
			/*
			 * TODO: current strategy is too simple.
			 */
			int n = (int) (totalSize / DEFAULT_CAPACITY) + 1;
			ByteBuffer old = data;
			data = ByteBuffer.allocateDirect(n * DEFAULT_CAPACITY);
			old.flip();
			data.put(old);
		}
		buff.flip();
		data.put(buff);
		return this;
	}

	public ByteBuffer getBuffer() {
		return data;
	}
}
