package com.semanticsquare.oop;

public class Staff extends User {
	public int id = 2;

	// public Staff() {
	// userType = "Staff";
	// System.out.println("Staff Constructor 1");
	// }

	public Staff(int id) {
		super(id);
		// this();
		// this.id = id;
		// System.out.println("Staff Constructor 2");
	}

	public Review postAReview(String reviewText) {
		System.out.println("Staff: postAReview");
		Review review = super.postAReview(reviewText);
		review.setApproved(true);
		return review;
	}

	public static void staticMethod() {
		System.out.println("Staff: staticMethod...");

	}
	// public void finalMethod() {
	// }

	void instanceMethod(int i) {
		System.out.println("Staff: instanceMethod....");
	}

	public void printId() {
		System.out.println("id: " + id);
		System.out.println("super.id: " + super.id);
	}

	public void printUserType() {
		System.out.println("Staff");
	}

}
