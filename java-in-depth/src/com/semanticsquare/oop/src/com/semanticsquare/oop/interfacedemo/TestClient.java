package com.semanticsquare.oop.interfacedemo;

public class TestClient {
	public static int getVal() {
		return 42;
	}

	public static void main(String... args) {
		// A a = new A();
		// a.foo();
		// a.bar();
		C c = new X();
		C.staticMethod();

		// default method demo
		// c.go();

		// new TestClient().lambdaTest(() -> System.out.println("Java In-depth"));
	}

	void lambdaTest(FunctionalInterface fi) {
		fi.test();
	}
}
