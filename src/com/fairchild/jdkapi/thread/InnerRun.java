package com.fairchild.jdkapi.thread;

public class InnerRun {
  private Thread internalThread;
  private volatile boolean noStopRequest;

  public InnerRun(){
    noStopRequest = true;
    Runnable runnable = new Runnable() {
      public void run() {
        try {
          runWork();
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }
    };

    internalThread = new Thread(runnable);
    internalThread.start();
  }

  private void runWork(){
    //it's while instead of if.
    while(noStopRequest){
      System.out.println("Running.");
      try{
        Thread.sleep(700);
      }catch(InterruptedException ie){
        System.out.println("Thread is interrupted");
      }
    }
  }

  public void setStopRequest(){
    noStopRequest = false;
    Thread.currentThread().interrupt();
  }

  public boolean isAlive(){
     return internalThread.isAlive();
  }
}