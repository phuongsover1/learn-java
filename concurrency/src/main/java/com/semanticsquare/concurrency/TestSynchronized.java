package com.semanticsquare.concurrency;

class Val {
	public static Integer i = 0;
}

class ThreadA extends Thread {

	@Override
	public void run() {
		synchronized (Val.i) {
			for (int i = 0; i < 5; i++) {
				Val.i += (int) (Math.random() * 1000);
				System.out.println("Thread A: " + Val.i);
			}
		}
	}

}

class ThreadB extends Thread {

	@Override
	public void run() {
		synchronized (Val.i) {
			for (int i = 0; i < 5; i++) {
				Val.i -= (int) (Math.random() * 1000);
				System.out.println("Thread B: " + Val.i);
			}
		}
	}

}

class Main {
	public static void main(String[] args) {
		new ThreadB().start();
		new ThreadA().start();
	}
}
