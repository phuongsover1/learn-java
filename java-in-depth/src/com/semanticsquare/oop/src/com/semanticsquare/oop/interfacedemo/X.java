
package com.semanticsquare.oop.interfacedemo;

public class X extends AbstractA implements A, B, C, Cloneable {
	@Override
	public void go() {
		staticMethod();
	}

	@Override
	public void foo() {
		System.out.println("X: foo");
		System.out.println("VAL: " + B.VAL);
	}

	@Override
	public void bar() {
		System.out.println("X: bar");
	}

	public C clone() {
		try {
			return (C) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void foobar() {
		System.out.println("X foobar:");
	}

}
