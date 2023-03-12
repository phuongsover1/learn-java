package com.semanticsquare.basics;

import java.util.ArrayList;
import java.util.List;

public class Student {

	static byte studentCount = 0;
	private static int idInitializer = 1000;
	private final int id;

	long phone;
	byte age;
	byte rank;
	String gender;
	String name;
	float gpa;
	char degree;
	boolean international;
	float tuitionFees = 12000.0f;
	float internationalFees = 5000.0f;

	Student(String name, String gender, byte age, long phone,
			float gpa, char degree, boolean international) {
		id = ++idInitializer;
		this.name = name;
		System.out.println("ID of" + name + " is " + id);
		this.gender = gender;
		this.age = age;
		this.phone = phone;
		this.gpa = gpa;
		this.degree = degree;
		this.international = international;

		if (international) {

			tuitionFees += internationalFees;
		}
		if (gpa > 3.5) {
			tuitionFees = 2000;
			tuitionFees -= 1000;
		}
		studentCount = (byte) (studentCount + 1);
		int nextId = id + 1;
		// System.out.println("\nid : " + this.id);
		// System.out.println("nextId : " + nextId);
		// System.out.println("name: " + this.name);
		// System.out.println("gender: " + this.gender); System.out.println("age: " +
		// this.age);
		// System.out.println("phone: " + this.phone);
		// System.out.println("gpa: " + this.gpa);
		// System.out.println("degree: " + this.degree);
		// System.out.println("tuitionFees: " + tuitionFees);
	}

	Student(String name, String gender, byte age, long phone,
			float gpa, char degree) {

		this(name, gender, age, phone,
				gpa, degree, false);

	}

	public boolean equals(Student s) {

		return id == s.id;
	}

	// public Student() {
	// }

	public void updateName(String name) {
		this.name = name;
	}

	public static void swap(Student[] students, int firstIndex, int secondIndex) {

		Student temp = students[firstIndex];
		students[firstIndex] = students[secondIndex];
		students[secondIndex] = temp;
	}

	public static void main(String[] args) {

		Student s1 = new Student("Phuong", "male", (byte) 21, 923_414_705, 3.8f, 'C', true);
		Student s2 = new Student("Luu", "male", (byte) 21, 321_456_789, 3.2f, 'A', true);
		Student s3 = new Student("Khang", "male", (byte) 23, 123_456_789, 3.9f, 'B');

		Student s4 = s1;
		System.out.println("s1 name: " + s1.name);
		System.out.println("s2 name: " + s2.name);
		System.out.println("s3 name: " + s3.name);
		System.out.println("s4 name: " + s4.name);
		s4.updateName("Phong");
		System.out.println("s1 name: " + s1.name);
		System.out.println("s4 name: " + s4.name);

		Student[] students = { s1, s2, s3 };
		List<Student> ew = new ArrayList<>();
		System.out.println("students[0] name: " + students[0].name);
		System.out.println("students[1] name: " + students[1].name);
		System.out.println("students[2] name: " + students[2].name);
		Student temp = students[0];
		students[0] = students[1];
		students[1] = students[2];
		students[2] = temp;
		System.out.println("students[0] name: " + students[0].name);
		System.out.println("students[1] name: " + students[1].name);
		System.out.println("students[2] name: " + students[2].name);

		swap(students, 0, 1);

		System.out.println("students[0] name: " + students[0].name);
		System.out.println("students[1] name: " + students[1].name);
		System.out.println("students[2] name: " + students[2].name);

	}

}
