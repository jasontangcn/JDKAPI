package com.fruits.jdkapi.reference;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

import junit.framework.Assert;

public class PhantomReferenceTest2 {
	public static class A {  
    static A a;  
    public void finalize() {  
        a = this;  
        System.out.println("x");
    }  
	}
	
	public static void main(String[] args) throws Exception {
		ReferenceQueue queue = new ReferenceQueue();  
	  
		PhantomReference ref = new PhantomReference(new A(), queue);  
		  
		Assert.assertNull(ref.get());  
		  
		Object obj = null;  
		  
		obj = queue.poll();  
		  
		Assert.assertNull(obj);  
		  
		System.gc();  
		  
		Thread.sleep(5000);  
		  
		System.gc();  
		  
		Assert.assertNull(ref.get());  
		  
		obj = queue.poll();  
		  
		Assert.assertNull(obj);  
		  
		A.a = null;  
		  
		System.gc(); 
		Thread.sleep(5000); 
		//System.gc();  
		obj = queue.poll();  
		  
		Assert.assertNotNull(obj);  
	}
}
