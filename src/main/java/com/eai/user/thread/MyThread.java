package com.eai.user.thread;

public class MyThread extends Thread{

 @Override
 public void run() {
     for (int i = 0; i < 6; i++) {
        System.out.println("Thread " + Thread.currentThread().getName() +" is running "+"count:"+i);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            System.out.println("Error");
        }
     }
 }
   

}
