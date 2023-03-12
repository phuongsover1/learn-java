package com.semanticsquare.basics;


public class StudentTest {

	public void testEnroll() {
		// Test to ensure that student is not allowed to enroll in more than the
		// limit

		// Setting input
		Student student = new Student("anita", "female");
		Course course1 = new Course(1, "Java In-Depth", null);

		// Method execution
		student.enroll(course1);

	}

}
