package com.fairchild.jdkapi.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import com.fairchild.jdkapi.Utils;

public class ClassInspector {
	public static void inspect(Class c) {
		//Annotation
		Utils.syslog("Class.getAnnotations:");
		for(Annotation anno : c.getAnnotations()) {
			Utils.syslog("	" + anno);
		}
		Utils.syslog("Class.getDeclaredAnnotations:");
		for(Annotation anno : c.getDeclaredAnnotations()) {
			Utils.syslog("	" + anno);
		}
		Utils.syslog("Class.getCanonicalName:\n	" + c.getCanonicalName());
		Utils.syslog("Class.getComponentType:\n	" + c.getComponentType());
		
		//c.getConstructor(parameterTypes);
		
		Utils.syslog("Class.getConstructors: ");
		for(Constructor cons : c.getConstructors()) {
			Utils.syslog("	" + cons);
		}
		
		Utils.syslog("Class.getDeclaredClasses: ");
		for(Class cls : c.getDeclaredClasses()) {
			Utils.syslog("	" + cls);
		}
		
		//c.getDeclaredConstructor(parameterTypes);
		
		Utils.syslog("Class.getDeclaredConstructors: ");
		for(Constructor cons : c.getDeclaredConstructors()) {
			Utils.syslog("	" + cons);
		}
		
		Utils.syslog("Class.getDeclaredFields: ");
		for(Field field : c.getDeclaredFields()) {
			Utils.syslog("	" + field);
		}
		
		//c.getField(name);
		Utils.syslog("Class.getFields: ");
		for(Field field : c.getFields()) {
			Utils.syslog("	" + field);
		}

		//c.getDeclaredMethod(name, parameterTypes);
		Utils.syslog("Class.getDeclaredMethods: ");
		for(Method method : c.getDeclaredMethods()) {
			Utils.syslog("	" + method);
		}

		//c.getMethod(name, parameterTypes);
		Utils.syslog("Class.getMethods: ");
		for(Method method : c.getMethods()) {
			Utils.syslog("	" + method);
		}
		
		Utils.syslog("Class.getDeclaringClass:\n	" + c.getDeclaringClass());
		
		Utils.syslog("Class.getEnclosingClass:\n	" + c.getEnclosingClass());
		
		Utils.syslog("Class.getEnclosingConstructor:\n	" + c.getEnclosingConstructor());
		Utils.syslog("Class.getEnclosingMethod:\n	" + c.getEnclosingMethod());
		
		
		Utils.syslog("Class.getGenericInterfaces: ");
		for(Type type : c.getGenericInterfaces()) {
			Utils.syslog("	" + type);
		}

		Utils.syslog("Class.getGenericSuperclass:\n	" + c.getGenericSuperclass());
		Utils.syslog("Class.getSuperclass:\n	" + c.getSuperclass());

		Utils.syslog("Class.getInterfaces: ");
		for(Type type : c.getInterfaces()) {
			Utils.syslog("	" + type);
		}
		
		Utils.syslog("Class.getModifiers:\n	" + c.getModifiers());

		Utils.syslog("Class.getSigners: ");
		Object[] signers = c.getSigners();
		if(null != signers) {
		  for(Object obj : signers) {
			  Utils.syslog("	" + obj);
	  	}
		}
		
		Utils.syslog("Class.getTypeParameters: ");
		for(TypeVariable type : c.getTypeParameters()) {
			Utils.syslog("	" + type);
		}
		
		Utils.syslog("Class.isAssignableFrom Flight:\n	" + c.isAssignableFrom(ReflectMisc.Flight.class));
		Utils.syslog("Class.isSynthetic:\n	" + c.isSynthetic());
	}
	
	public static void main(String[] args) {
		ClassInspector.inspect(ReflectMisc.SuperMan.class);
	}
}
