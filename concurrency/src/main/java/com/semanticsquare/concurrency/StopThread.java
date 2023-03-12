package com.semanticsquare.concurrency;

import java.util.concurrent.TimeUnit;

class StopThread {
	private static boolean stop;

	public static void main(String[] args) {
		new Thread(new Runnable() {
			public void run() {
				while (!stop)
					System.out.println("In while ...");
			}
		}).start();

		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		stop = true;
		System.out.println("Inner main ...");
	}
}
