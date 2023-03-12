package com.semanticsquare.concurrency;

public class MyFirstThread2 extends Thread {

	@Override
	public void run() {
		System.out.println("Inside thread ...");
		go();
	}

	private void go() {
		System.out.println("Inside go ...");
		more();
	}

	private void more() {
		System.out.println("Inside more ...");
	}

	public static void main(String[] args) {
		Thread thread = new MyFirstThread2();
		thread.start();
		System.out.println("Inside main ... ");
	}

}
