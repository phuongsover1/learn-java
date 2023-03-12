package com.semanticsquare.oop;

public class User {
	public int id = 1;
	public String userType = "User";

	// public User() {
	// System.out.println("User Constructor");
	// }

	User(int id) {
	}

	public void displayUserInfo() {
		System.out.println(this);
	}

	public void saveWebLink() {
		System.out.println("User: saveWebLink");
		postAReview("");
		staticMethod();
	}

	// Method binding demo
	public static void staticMethod() {
		System.out.println("\nUser: staticMethod");
	}

	public final void finalMethod() {
	}

	void instanceMethod(double d) {
		System.out.println("User: instanceMethod....");
	}

	public void printUserType() {
		System.out.println("User");
	}

	public Review postAReview(String reviewText) {
		System.out.println("User: postAReview");
		Review review = new Review(reviewText);
		return review;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("User [id=").append(id).append(", userType=").append(userType).append("]");
		return result.toString();

	}

}
