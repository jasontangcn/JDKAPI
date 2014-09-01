package com.fairchild.jdkapi.reference;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

import com.fairchild.jdkapi.JDKAPIUtils;
import com.fairchild.jdkapi.reference.ReferenceMisc.RefInnerClass;

//SoftReference and WeakReference, almost the same.
public class SoftReferenceTest {
	public static void main(String[] args) {
		ReferenceMisc.RefInnerClass refInnerObj = new ReferenceMisc.RefInnerClass();
		ReferenceQueue queue = new ReferenceQueue();
		SoftReference<ReferenceMisc.RefInnerClass> softRef = new SoftReference<ReferenceMisc.RefInnerClass>(refInnerObj, queue);
		refInnerObj = null;
		
		for(;;) {
			System.out.println ("Polling reference queue.");
		  System.gc();
		  if(null != queue.poll())
		  	break;
		}
		JDKAPIUtils.syslog(softRef.get());
		
	}
}
