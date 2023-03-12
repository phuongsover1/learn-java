package com.semanticsquare.basics;

import java.util.HashMap;

public class BasicsDemo {
	static HashMap<String, String> map = new HashMap<String, String>();
	static {
		map.put("John", "111_222_333");
		map.put("Anita", "222_333_444");
	}

	// static int[] scores;

	static void arrays() {
		System.out.println("\nInside arrays....");
		// int[] scores = new int[4];
		int[] scores = new int[] { 70, 80, 90, 100 };
		// int[] scores = {70,90,80,70};
		/// *int[]*/ scores = {70,90,80,70};

		System.out.println("\nMid-term 1: " + scores[0]);
		System.out.println("\nMid-term 2: " + scores[1]);
		System.out.println("\nFinal : " + scores[2]);
		System.out.println("\nProject : " + scores[3]);
		System.out.println("\n# Exams : " + scores.length);

		// Student[] students = new Student[3]; // ok
		// Student[] students = new Student[] {new Student(), new Student(), new
		// Student()};
		// Student[] students = { new Student(), new Student(), new Student() };
		/*
		 * students[0] = new Student();
		 * students[1] = new Student();
		 * students[2] = new Student();
		 */
		// System.out.println("Student 1: " + students[0]);
		// System.out.println("Student 2: " + students[1]);
		// System.out.println("Student 3: " + students[2]);

	}

	static double sum(double a, double b) {
		return a + b;
	}

	static double avg(double a, double b) {
		double sum = sum(a, b);
		return sum / 2;

	}

	static boolean doSomething(int[] arr, double b) {
		return true;
	}

	static void go(int[] array) {
		System.out.println("array[0]: " + array[0]);
		System.out.println("array[1]: " + array[1]);
		array[1] = 22;

	}

	static void go(int i) {
		System.out.println("Go in go(int i).");
	}

	static void go(short s) {
		System.out.println("Go in go(short s)");

	}

	static void varargsOverload(boolean b, int i, int j, int k) {
		System.out.println("\nInside varargsOverload without varargs ...");
	}

	static void varargsOverload(boolean b, int... list) {
		System.out.println("\nInside varargsOverload with varargs ...");
		System.out.println("list.length: " + list.length);

	}

	void foo() {
		System.out.println("Inside foo!!!!!!!!!!");
	}

	static void stringExamples() {
		System.out.println("\nInside stringExamples ...");
		String s = "hello world!";
		System.out.println("s: " + s);

		// System.out.println("\ns.length(): " + s.length());
		// System.out.println("s.isEmpty(): " + s.isEmpty());

		// Comparison
		// System.out.println("\ns.equals(\"HELLO WORLD!\"): " + s.equals("HELLO
		// WORLD!"));
		// System.out.println("\ns.equalsIgnoreCase(\"HELLO WORLD!\"): " +
		// s.equalsIgnoreCase("HELLO WORLD!"));
		// System.out.println("\ns.compareTo(\"HELLO WORLD!\"): " + s.compareTo("HELLO
		// WORLD!"));

		// Search

		// System.out.println("\ns.contains(\"HELLO WORLD!\"): " + s.contains("HELLO
		// WORLD!"));
		// System.out.println("\ns.contains(\"world!\"): " + s.contains("world!"));
		// System.out.println("\ns.startsWith(\"HELLO WORLD!\"): " + s.startsWith("HELLO
		// WORLD!"));
		// System.out.println("\ns.startsWith(\"hello world!\"): " + s.startsWith("hello
		// world!"));
		// System.out.println("s.endsWith(\"!\"): " + s.endsWith("!"));
		// System.out.println("s.indexOf(\"lo\"): " + s.indexOf("lo"));
		// System.out.println("s.indexOf(\"o\"): " + s.indexOf('o'));
		// System.out.println("s.lastIndexOf(\"o\"): " + s.lastIndexOf('o'));

		// // Examining individual characters
		// System.out.println("\ns.charAt(4): " + s.charAt(4));

		// // Extracting substrings
		// System.out.println("\ns.substring(4): " + s.substring(4));
		// System.out.println("s.substring(4,9): " + s.substring(4, 12));

		// // Case conversions (Note: String is immutable. So, only a copy is returned)
		// System.out.println("\ns.toUpperCase(): " + s.toUpperCase());
		// System.out.println("s.toLowerCase(): " + s.toLowerCase());

		// System.out.println("\ns.trim(): " + s.trim()); // return a copy of string
		// after trimming any leading and
		// // trailing whitespace, but not between

		// Replace (e.g., replace comma's with white-space)
		// System.out.println("\ns.replaceAll(\"o\",\"r\"): " + s.replaceAll("o", "r"));

		// Split (e.g., split a document into words or pslit a line of text by tab or
		// comma or white space)
		// System.out.println("\ns.split(\"o\"): ");
		// String[] sa = s.split("o");
		// for (String temp : sa) {
		// System.out.println(temp);
		// }

		// Static method (includes overloaded methods)
		// System.out.println("\nString.valueOf(1.3): " + String.valueOf(1.3));

	}

	static void stringPool() {
		System.out.println("\nInside stringPool ...");
		String s1 = "hello!";
		String s2 = "hello!";
		String s3 = "hello!".intern();
		String s4 = new String("hello!");
		String s5 = "lo!";

		System.out.println("s1 == s2: " + (s1 == s2));
		System.out.println("s1 == s3: " + (s1 == s3));
		System.out.println("s1 == s4: " + (s1 == s4));
		System.out.println("s1 == s4.intern(): " + (s1 == s4.intern()));
		System.out.println("s1 == \"hel\" + \"lo!\": " + (s1 == "hel" + "lo!"));
		System.out.println("s1 == \"hel\" + s5: " + (s1 == "hel" + s5));
	}

	static void mutableString() {
		StringBuffer sb = new StringBuffer("Hello World");
		sb.append(" good").append(" morning :)");
		System.out.println(sb);
	}

	public BasicsDemo() {
		System.out.println("Inside no-args constructor ... ");
	}

	public BasicsDemo(int id) {
		this();
		System.out.println("Inside constructor with parameter ...");
	}

	{
		System.out.println("Inside instance initializer ...");
	}

	public static void main(String... args) {
		// stringExamples();
		// stringPool();
		// mutableString();

		// System.out.println(map.get("John"));
		// System.out.println(map.get("Anita"));

		// new BasicsDemo();
		// new BasicsDemo(12);

		boxedPrimities();
	}

	private static void boxedPrimities() {
		Integer boxedInt = Integer.valueOf(8);
		Boolean boxedBoolean = Boolean.valueOf(true);
		Double boxedDouble = Double.valueOf(25.5);
		Character boxedCharacter = Character.valueOf('c');

		// System.out.println(boxedInt);
		// System.out.println(boxedBoolean);
		// System.out.println(boxedDouble);
		// System.out.println(boxedCharacter);

		// Integer boxedInteger1 = new Integer(8); // not recommended

		// unwrap: typeValue
		int primitiveInt = boxedInt.intValue();
		boolean primitiveBoolean = boxedBoolean.booleanValue();
		double primitiveDouble = boxedDouble.doubleValue();
		char primitiveChar = boxedCharacter.charValue();

		// System.out.println(primitiveInt);
		// System.out.println(primitiveBoolean);
		// System.out.println(primitiveDouble);
		// System.out.println(primitiveChar);

		// 3. utility methods
		// char genre = 'T';
		// boolean isLetter = Character.isLetter(genre);
		// boolean isDigit = Character.isDigit(genre);
		// boolean isLetterOrDigit = Character.isLetterOrDigit(genre);
		// boolean isUppercase = Character.isUpperCase(genre);

		// System.out.println(isLetter);
		// System.out.println(isDigit);
		// System.out.println(isLetterOrDigit);
		// System.out.println(isUppercase);

		// long start = System.nanoTime();
		// veryExpensive();
		// System.out.println("Elapsed time: " + ((System.nanoTime() - start) /
		// 1_000_000.0) + " mseconds.");
		// compareBoxPrimitives();

		unbelivable();
	}

	static Integer i;

	private static void unbelivable() {
		if (i == 0) {
			System.out.println("weird!");
		}

	}

	// private static void compareBoxPrimitives() {
	// System.out.println("\nInside compareBoxPrimitives ... ");
	// Integer num1 = new Integer(0);
	// Integer num2 = new Integer(0);
	// System.out.println(" (num1 == num2): " + (num1 == num2));

	// // Solutions:
	// System.out.println(" (num1.intValue() == num2.intValue()): " +
	// (num1.intValue() == num2.intValue()));
	// System.out.println(" (num1.equals(num2)): " + (num1.equals(num2)));

	// Integer num3 = new Integer(1);
	// System.out.println(" (num1 < num3): " + (num1 < num3)); // "<" does un-boxing
	// first
	// }

	private static void veryExpensive() {
		System.out.println("In veryExpensive() ...");
		long sum = 0L;
		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			sum += i;
		}
	}

}
