package com.fruits.jdkapi.reference;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

import com.fruits.jdkapi.JDKAPIUtils;
import com.fruits.jdkapi.reference.ReferenceMisc.RefInnerClass;

public class PhantomReferenceSubclass extends PhantomReference {
	private String name;
	
	public PhantomReferenceSubclass(String name, RefInnerClass refInner, ReferenceQueue queue) {
		super(refInner, queue);
		this.name = name;
	}
	
	public void deleteFile() {
		JDKAPIUtils.syslog("Delete the file with name " + name);
	}
}
