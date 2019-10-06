package com.fruits.jdkapi.reflect;

import java.lang.reflect.Field;

import com.fruits.jdkapi.JDKAPIUtils;

public class SetFieldAccessible {
	public static class SetFieldAccessibleInnerClass {
		private String name;
		
		public SetFieldAccessibleInnerClass(String name) {
			this.name = name;
		}
		
		private void setName(String name) {
			this.name = name;
		}
	}
	
	public static void main(String[] args) throws Exception {
		SetFieldAccessibleInnerClass o = new SetFieldAccessibleInnerClass("Hello");
		Class c = SetFieldAccessibleInnerClass.class;
		Field nameField = c.getDeclaredField("name");
		nameField.setAccessible(true);
		JDKAPIUtils.syslog(nameField.get(o));
	}
}
