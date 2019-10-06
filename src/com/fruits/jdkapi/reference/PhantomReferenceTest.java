package com.fruits.jdkapi.reference;

import java.lang.ref.ReferenceQueue;

import com.fruits.jdkapi.reference.ReferenceMisc.RefInnerClass;

public class PhantomReferenceTest {

	public static void main(String[] args) {
		final ReferenceQueue queue = new ReferenceQueue();
		RefInnerClass refInner = new RefInnerClass();
		PhantomReferenceSubclass phantomRef = new PhantomReferenceSubclass("RefInnerClass",refInner, queue);
		new Thread(new Runnable(){
	    public void run() {
	    	try{
		    	for(;;) {
		    		PhantomReferenceSubclass ref = (PhantomReferenceSubclass)queue.remove();
		    		ref.deleteFile();
		    		ref.clear(); // Important!
		    	}
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}
	    }
		}).start();
	}

}
