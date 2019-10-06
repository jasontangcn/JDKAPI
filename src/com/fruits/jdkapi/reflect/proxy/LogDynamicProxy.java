package com.fruits.jdkapi.reflect.proxy;

import java.lang.reflect.Proxy;

import com.fruits.jdkapi.reflect.ReflectMisc;

public class LogDynamicProxy {

	public static void main(String[] args) {
		ReflectMisc.Action work = new ReflectMisc.ActionImpl();
		ReflectMisc.Action proxy = (ReflectMisc.Action)Proxy.newProxyInstance(ReflectMisc.Action.class.getClassLoader(), new Class[]{ReflectMisc.Action.class}, new LogHandler(work));
		proxy.doSomething();
	}

}
