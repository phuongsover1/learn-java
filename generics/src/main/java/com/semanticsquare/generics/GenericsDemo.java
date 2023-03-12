package com.semanticsquare.generics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

class GenericsDemo<T extends List & Serializable> {

	public static void rawTypeTest() {
		System.out.println("\n\nInside rawTypeTest ...");
		int ISBN = 1505297729;
		List<Double> prices = new ArrayList<>();

		HaflIntergrator.getPrice(ISBN, prices);
		Double price = prices.get(0);
		// Khi compile thi Compiler tu dong explicit cast nhu nay
		// Double price = (Double) prices.get(0);
	}

	private static void getCommonElementCount(List l1, List l2) {
		int count = 0;
		for (Object element : l1) {
			if (l2.contains(element))
				++count;
		}
		System.out.println("common element: " + count);
	}

	private static void getCommonElementCountWithWildcard(List<?> l1, List<?> l2) {
		int count = 0;
		for (Object element : l1) {
			if (l2.contains(element))
				++count;
		}
		System.out.println("common element: " + count);

	}

	// private static void invalidAggregate(List<?> l1, List<?> l2, List<?> l3) {
	// l3.addAll(l1);
	// l3.addAll(l2);
	// }

	// private static <T, T1, E> boolean replaceAll(List<T> list, T oldVal, T1
	// newVal) {
	// list.add(oldVal);

	// }

	private static void genericMethodDemo() {
		System.out.println("\n\nInside genericsMedthodDemo");

		// Type argument inference via method argument
		typeArgInference(22.0);
		typeArgInference("Java");

		// Double number = typeArgInference1("Java");

		// Type arg inference in method invocation context ~ works from Java 8
		GenericsDemo.targetTypeInvoker1(typeArgInferenceFromTargetType2());

		GenericsDemo.targetTypeInvoker2(typeArgInferenceFromTargetType2());

		List<String> strList = GenericsDemo.targetTypeInvoker2(typeArgInferenceFromTargetType2());
	}

	private static <T> List<T> typeArgInferenceFromTargetType2() {
		List<String> list = new ArrayList<>();
		list.add("abc");
		return (List<T>) list;
	}

	private static void targetTypeInvoker1(List<String> l) {

	}

	private static <T> List<T> targetTypeInvoker2(List<T> l) {
		return l;
	}

	private static <T> T typeArgInference1(T object) {
		System.out.println("Type argument: " + object.getClass().getName());
		return object;
	}

	public static <T> void typeArgInference(T object) {
		System.out.println("Type Argument: " + object.getClass().getName());
	}

	private static void boundedWildcards() {
		System.out.println("\n\n");
		List<Integer> intList = Arrays.asList(11, 21, 31);
		display(intList);
		List<Double> doubleList = Arrays.asList(11.5, 21.5, 31.5);
		display(doubleList);

		List<Number> numList = new ArrayList<>();
		aggregateWthConsumer(intList, doubleList, numList);
		System.out.println("'nDisplay numList: '");
		display(numList);
	}

	private static void display(List<? extends Number> list) {
		for (Number element : list) {
			System.out.println("display()/element: " + element);
		}
	}

	private static <E> void aggregateWthConsumer(List<? extends E> l1, List<? extends E> l2, List<? super E> l3) {
		l3.addAll(l1);
		l3.addAll(l2);
	}

	public static void main(String... args) {
		boundedWildcards();
		// genericMethodDemo();

		// List<String> strList1 = Arrays.asList("a", "b", "c");
		// List<String> strList2 = Arrays.asList("b", "c", "d");
		// // getCommonElementCount(strList1, strList2);
		// getCommonElementCountWithWildcard(strList1, strList2);
		// // rawTypeTest();
		// Container<String> stringStore = new Store<>();
		// stringStore.set("Phuong");
		// // stringStore.set(1);
		// // System.out.println(stringStore.get());

		// Container<Integer> integerStore = new Store<>();
		// integerStore.set(1);

		// Container<?> someStore = stringStore;
		// System.out.println(someStore.get());

		// someStore = integerStore;
		// System.out.println(someStore.get());

		// // List<Integer> intList1 = Arrays.asList(1, 2);
		// // List<Integer> intList2 = Arrays.asList(3, 4);
		// // invalidAggregate(intList1, intList2, new ArrayList<>());

		// // System.out.println(integerStore.get());

		// // Container<List<Integer>> listStore = new Store<>();
		// // listStore.set(Arrays.asList(1, 5, 7, 2));
		// // System.out.println(listStore.get());

		// // List<Number> list = new ArrayList<>();
		// // list.add(new Integer(1));
		// // list.add(new Double(200.0));

		// // List[] array = new ArrayList[2];
		// // array[0] = new ArrayList<>();
		// // array[1] = new LinkedList<>();

		// // GenericsDemo<List> genericsList = new GenericsDemo<>();
		// // GenericsDemo<LinkedList> linkedList = new GenericsDemo<>();
		// // GenericsDemo<Collection> collection = new GenericsDemo<>();

		// // go(new ArrayList<Integer>());
		// go(new Integer[3]);
	}

	// Invariance
	private static void go(List<Number> list) {

	}

	// Covariance
	private static void go(Number[] list) {
		list[0] = 24.4;
	}

}

class HaflIntergrator {

	public static void getPrice(int ISBN, List prices) {
		prices.add(45);
	}
}

interface Container<T> {
	void set(T a);

	T get();
}

class Store<T> implements Container<T> {
	private T a;

	public void set(T a) {
		this.a = a;
	}

	public T get() {
		return a;
	}

}
