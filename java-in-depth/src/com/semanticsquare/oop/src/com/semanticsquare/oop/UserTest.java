package com.semanticsquare.oop;

public class UserTest {
	public void printUserType(User u) {
		u.printUserType();

	}

	public void approveReview(Staff s) {
		if (s instanceof Editor) {
			((Editor) s).approveReview();
		} else {
			System.out.println("Invalid object passed ... ");
		}
	}

	public static void main(String... args) {
		// Overrding of instance variables demo
		User staff = new Staff(3);
		System.out.println(staff);
		// User staff2 = staff;
		// System.out.println(staff.hashCode());
		// System.out.println(staff2.hashCode());
		// staff.displayUserInfo();
		// ((Staff) staff).displayUserInfo();
		// staff.saveWebLink();
		// ((Staff) staff).staticMethod();
		// staff.staticMethod();
		// staff.staticMethod();
		// staff.postAReview("");
		// staff.instanceMethod(10);
	}

}
