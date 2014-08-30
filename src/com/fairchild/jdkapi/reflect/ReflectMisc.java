package com.fairchild.jdkapi.reflect;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


public class ReflectMisc {
	//Used by Reflection
	@Target(ElementType.TYPE)  
	@Retention(RetentionPolicy.RUNTIME)  
	@Documented
	public static @interface NewAnnotation {	
		String value();
	}
	
	public static interface People {
		public void walk();
	}

	public static interface Gun {
		public void fire();
	}
	
	public static class Planet {
		public void fly() {
			System.out.println("Tree.breath");
		}
	}
	
	@NewAnnotation(value="NewAnnotation")
	public static class SuperMan extends Planet implements People, Gun  {
		public void walk() {
			System.out.println("SuperMan.walk");
		}
		public void fire () {
			System.out.println("SuperMan.fire");
		}
	}
	
	
	// Used by DynamicProxy
	public static interface Action {
		public void doSomething();
	}
	
	public static class ActionImpl implements Action {
		public void doSomething() {
			System.out.println("Hello World!");
		}
	}
}


