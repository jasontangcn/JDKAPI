package com.fruits.jdkapi.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import com.fruits.jdkapi.JDKAPIUtils;

public class ClassInspector {
	public static void inspect(Class c) {
		//Annotation
		JDKAPIUtils.syslog("Class.getAnnotations:");
		for(Annotation anno : c.getAnnotations()) {
			JDKAPIUtils.syslog("	" + anno);
		}
		JDKAPIUtils.syslog("Class.getDeclaredAnnotations:");
		for(Annotation anno : c.getDeclaredAnnotations()) {
			JDKAPIUtils.syslog("	" + anno);
		}
		JDKAPIUtils.syslog("Class.getCanonicalName:\n	" + c.getCanonicalName());
		JDKAPIUtils.syslog("Class.getComponentType:\n	" + c.getComponentType());
		
		//c.getConstructor(parameterTypes);
		
		JDKAPIUtils.syslog("Class.getConstructors: ");
		for(Constructor cons : c.getConstructors()) {
			JDKAPIUtils.syslog("	" + cons);
		}
		
		JDKAPIUtils.syslog("Class.getDeclaredClasses: ");
		for(Class cls : c.getDeclaredClasses()) {
			JDKAPIUtils.syslog("	" + cls);
		}
		
		//c.getDeclaredConstructor(parameterTypes);
		
		JDKAPIUtils.syslog("Class.getDeclaredConstructors: ");
		for(Constructor cons : c.getDeclaredConstructors()) {
			JDKAPIUtils.syslog("	" + cons);
		}
		
		JDKAPIUtils.syslog("Class.getDeclaredFields: ");
		for(Field field : c.getDeclaredFields()) {
			JDKAPIUtils.syslog("	" + field);
		}
		
		//c.getField(name);
		JDKAPIUtils.syslog("Class.getFields: ");
		for(Field field : c.getFields()) {
			JDKAPIUtils.syslog("	" + field);
		}

		//c.getDeclaredMethod(name, parameterTypes);
		JDKAPIUtils.syslog("Class.getDeclaredMethods: ");
		for(Method method : c.getDeclaredMethods()) {
			JDKAPIUtils.syslog("	" + method);
		}

		//c.getMethod(name, parameterTypes);
		JDKAPIUtils.syslog("Class.getMethods: ");
		for(Method method : c.getMethods()) {
			JDKAPIUtils.syslog("	" + method);
		}
		
		JDKAPIUtils.syslog("Class.getDeclaringClass:\n	" + c.getDeclaringClass());
		
		JDKAPIUtils.syslog("Class.getEnclosingClass:\n	" + c.getEnclosingClass());
		
		JDKAPIUtils.syslog("Class.getEnclosingConstructor:\n	" + c.getEnclosingConstructor());
		JDKAPIUtils.syslog("Class.getEnclosingMethod:\n	" + c.getEnclosingMethod());
		
		
		JDKAPIUtils.syslog("Class.getGenericInterfaces: ");
		for(Type type : c.getGenericInterfaces()) {
			JDKAPIUtils.syslog("	" + type);
		}

		JDKAPIUtils.syslog("Class.getGenericSuperclass:\n	" + c.getGenericSuperclass());
		JDKAPIUtils.syslog("Class.getSuperclass:\n	" + c.getSuperclass());

		JDKAPIUtils.syslog("Class.getInterfaces: ");
		for(Type type : c.getInterfaces()) {
			JDKAPIUtils.syslog("	" + type);
		}
		
		JDKAPIUtils.syslog("Class.getModifiers:\n	" + c.getModifiers());

		JDKAPIUtils.syslog("Class.getSigners: ");
		Object[] signers = c.getSigners();
		if(null != signers) {
		  for(Object obj : signers) {
			  JDKAPIUtils.syslog("	" + obj);
	  	}
		}
		
		JDKAPIUtils.syslog("Class.getTypeParameters: ");
		for(TypeVariable type : c.getTypeParameters()) {
			JDKAPIUtils.syslog("	" + type);
		}
		
		JDKAPIUtils.syslog("Class.isAssignableFrom Flight:\n	" + c.isAssignableFrom(ReflectMisc.Flight.class));
		JDKAPIUtils.syslog("Class.isSynthetic:\n	" + c.isSynthetic());
	}
	
	public static void main(String[] args) {
		ClassInspector.inspect(ReflectMisc.SuperMan.class);
	}
}
