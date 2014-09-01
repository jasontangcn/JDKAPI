package com.fairchild.jdkapi.reference;

public class ReferenceMisc {

	public static class RefInnerClass {
		protected void finalize() throws Throwable {
			System.out.println("finalize is called.");
		}
	}

}
