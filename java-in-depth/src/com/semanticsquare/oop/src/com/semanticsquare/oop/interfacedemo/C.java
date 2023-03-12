package com.semanticsquare.oop.interfacedemo;

public interface C {
	void foobar();

	default void go() {
		System.out.println("C: go()");
	}

	static void staticMethod() {
		System.out.println("C: StaticMethod");
	}
}
