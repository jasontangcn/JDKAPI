package com.fruits.jdkapi.reflect;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
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
	
	@Inherited
	@Target(ElementType.TYPE)  
	@Retention(RetentionPolicy.RUNTIME)  
	@Documented
	public static @interface HotAnnotation {	
		String value();
	}
	
	public static interface People {
		public void walk();
	}
	
	public static interface Flight {
		public void fly();
	}
	
	public static interface Weapon {
		public float weight();
	}

	public static interface Gun extends Weapon {
		public void fire();
	}
	
	@HotAnnotation(value="HotAnnotation")
	public static class Airplane implements Weapon, Flight {
		public static final String AIRPLANE_AIRBUS = "AirBus";
		
		protected String name;
		
		public Airplane() {
		}
		
		public Airplane(String name) {
			this.name = name;
		}
		
		public float weight() {
			return 512;
		}
		
		public void fly() {
			System.out.println("Airplane.fly");
		}
	}
	
	@NewAnnotation(value="NewAnnotation")
	public static class SuperMan extends Airplane implements People, Gun  {
		public SuperMan(){
			
		}
		
		public SuperMan(String name) {
			this.name = name;
		}
		
		public float weight() {
			return 0;
		}
		public void walk() {
			System.out.println("SuperMan.walk");
		}
		public void fire () {
			System.out.println("SuperMan.fire");
		}
		
		public class SuperManInnerClass {
			
		}
		
		private void fix() {
			
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


