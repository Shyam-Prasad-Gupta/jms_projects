package com.shyam.core.thread.jms;

public class DriverProgramThread {
	
	public static void main(String args[]) throws InterruptedException {
		
		thread(new HelloWorldProducerThread(), false);//p1
		thread(new HelloWorldProducerThread(), false);//p2
		thread(new HelloWorldProducerThread(), false);//p3
		thread(new HelloWorldProducerThread(), false);//p4
		thread(new HelloWorldProducerThread(), false);//p5
		thread(new HelloWorldProducerThread(), false);//p6
		thread(new HelloWorldConsumerThread(), false);//c1
		Thread.sleep(1000);
		thread(new HelloWorldConsumerThread(), false);//c2
		thread(new HelloWorldConsumerThread(), false);//c3
		thread(new HelloWorldProducerThread(), false);//p7
		thread(new HelloWorldConsumerThread(), false);//c4
		thread(new HelloWorldConsumerThread(), false);//c5
		thread(new HelloWorldConsumerThread(), false);//c6
		Thread.sleep(1000);
		
	}
	
	public static void thread(Runnable runnable, boolean daemon) {
		Thread newThread = new Thread(runnable);
		newThread.setDaemon(daemon);
		newThread.start();
	}
	
}
