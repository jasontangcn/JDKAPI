/*
 * Created on 2005-9-21
 *
 */
package com.fairchild.jdkapi.nio.samples.test;

/**
 * @author TomHornson@hotmail.com
 *
 */
public class RunnableTest extends Thread{
    private int num = 0;
    
    public RunnableTest(int num){
        this.num = num;        
    }
    
    public void run(){
    	num = 10000;   
        System.out.println(num);
    }
    
    public void set(int num){
        this.num = num;
    }
    
    public void print(){
        System.out.println(Thread.currentThread().getName()+ " : " + num);
        System.out.println(1.0e3);
    }
    
    public static void main(String[] args) {
        RunnableTest rt = new RunnableTest(100);
        rt.start();
        rt.print();
    }
}
