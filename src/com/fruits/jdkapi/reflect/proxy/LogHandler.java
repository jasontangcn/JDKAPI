package com.fruits.jdkapi.reflect.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.fruits.jdkapi.reflect.ReflectMisc.Action;


class LogHandler implements InvocationHandler {
	private Action action;
	
	public LogHandler(Action action) {
		this.action = action;
	}
	
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    	StringBuffer sb = new StringBuffer();
    	sb.append("Object: " + action).append("\n")
    	.append("  Method: " + method.getName()).append("\n")
    	.append("  Arguments: " + args).append("\n");
    	System.out.println(sb.toString());
    	method.invoke(action, args);
    	return null;
    }
}