
package com.semanticsquare.enums;

public class Test {

	public static void main(String[] args) {
		Integer num1 = new Integer("25");
		Integer num2 = new Integer("25");
		System.out.println(num1 == num2);

		B bObject = new B(3);
		StringBuilder string = new StringBuilder("fdfd");
		B cObject = new B(4);
		System.out.println(bObject.getId());
		System.out.println(cObject.getId());
		System.out.println(bObject.getId());
	}

}

class User {
	private int id;

	public User(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}

class Staff extends User implements AInterface, BInterface {
	public Staff(int id) {
		super(id)
	}
}

interface AInterface {
	void bar();
}

interface BInterface {
	boolean bar();
	String
}
