package com.semanticsquare.concurrency;

class RaceConditionDemo {
	
	public static void main(String[] args) {
		BankAccount task = new BankAccount();
		task.setBalance(100);

		Thread john = new Thread(task);
		Thread anita = new Thread(task);

		john.setName("john");
		anita.setName("anita");
		john.start();
		anita.start();
	}
}

class BankAccount implements Runnable {
	private int balance;

	public void setBalance(int balance) {
		this.balance = balance;
	}

	@Override
	public void run() {
		makeWithdrawal(75);
		if (balance < 0) {
			System.out.println("Money overdrawn !!!");
		}

	}

	private synchronized void makeWithdrawal(int amount) {
		if (balance >= amount) {
			System.out.println(Thread.currentThread().getName() + " is about to withdraw ...");
			balance -= amount;
			System.out.println(Thread.currentThread().getName() + " has withdrawn " + amount);
		} else {
			System.out.println(
					"Sorry, not enough balance for " + Thread.currentThread().getName() + " to withdrawal " + amount);
		}
	}

}
