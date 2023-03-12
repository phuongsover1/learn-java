package com.semanticsquare.oop.interfacedemo;

public abstract class AbstractA implements C {
	public void bar() {
		System.out.println("AbstractA: bar");
	}

	@Override
	public void go() {
		System.out.println("Abstract A: go()");
	}

}
