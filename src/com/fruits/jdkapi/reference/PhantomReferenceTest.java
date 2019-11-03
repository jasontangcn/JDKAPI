package com.fruits.jdkapi.reference;

import java.lang.ref.ReferenceQueue;

import com.fruits.jdkapi.reference.ReferenceMisc.RefInnerClass;

public class PhantomReferenceTest {

	public static void main(String[] args) throws Exception {
		final ReferenceQueue queue = new ReferenceQueue();
		RefInnerClass refInner = new RefInnerClass();
		PhantomReferenceSubclass phantomRef = new PhantomReferenceSubclass("RefInnerClass",refInner, queue);
		refInner = null;
		
		System.gc(); // invoke 'finalize' and object will be in status 'finalized'.
		
		Thread.sleep(5000);
		
		System.gc(); // GC check the object again, if object is in status 'reclaimable' status, and is only phantom reachable object(not reborn), 
		             // equeue it then.
		
		PhantomReferenceSubclass ref = (PhantomReferenceSubclass)queue.remove();
		System.out.println(ref);
		
		/*
		new Thread(new Runnable(){
	    public void run() {
	    	try{
		    	for(;;) {
		    		System.out.println("trying to get a object from queue.");
		    		PhantomReferenceSubclass ref = (PhantomReferenceSubclass)queue.remove();
		    		System.out.println("got a object from queue.");
		    		ref.deleteFile();
		    		ref.clear(); // Important!
		    	}
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}
	    }
		}).start();
		*/
	}

}
