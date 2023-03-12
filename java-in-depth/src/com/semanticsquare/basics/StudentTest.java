package com.semanticsquare.basics;

// import com.semanticsquare.basics.BasicsDemo;
/**
 * Demonstrates Java's basic language features
 * 
 * @author Phuong
 *
 */
public class StudentTest {
	// public static final int MAX_VALUE;

	// static {
	// MAX_VALUE = 3;
	// }

	static final byte month2;
	static {
		month2 = 2;
	}

	static void switchExample() {
		System.out.println("\nInside swithExample ... ");
		byte month = 3;
		// byte month2 = 2;
		switch (month) {
			case 1:
				System.out.println("January");
				break;
			case 2:
				System.out.println("February");
				break;
			case 3:
				System.out.println("March");
				break;
			default:
				System.out.println("April");
		}
	}

	public void sayHello() {
		System.out.println("Inside studentTest.java");
	}

	public static void main(String... args) {
		// Student s1 = new Student("Phuong", "male", (byte) 21, 923_414_705,
		// 3.8f, 'C', true);
		// Student s2 = new Student("Luu", "male", (byte) 21, 321_456_789, 3.2f,
		// 'A', true);
		// Student s3 = new Student("Khang", "male", (byte) 23, 123_456_789, 3.9f,
		// 'B');
		// // new BasicsDemo().foo();
		// // HashSet ~ s, c, f

		// Student s4 = new Student("Khang", "male", (byte) 23, 123_456_789, 3.9f,
		// 'B');
		// // boolean isDuplicate = s3.equals(s4);
		// // System.out.println(isDuplicate);

		// System.out.println(MAX_VALUE);
		System.out.println("Inside StudentTest.java ...");
	}

}
