package com.semanticsquare.lambdas;

import java.util.Arrays;
import java.util.Comparator;

interface CacheIterator {
	boolean hasNext();

	Bookmark next();

}

class FooBar {
	protected int y;

	FooBar(int x) {
		y = x;
	}
}

class Cache {

	public Cache() {
	}

	private Bookmark[] items;
	private int next = 0;
	private static final Comparator<Bookmark> RATING_COMPARATOR = new Comparator<Bookmark>() {
		{
			System.out.println("Instance initializer");
		}

		@Override
		public int compare(Bookmark o1, Bookmark o2) {
			return o1.getRating() - o2.getRating() > 0 ? 1 : -1;
		}

	};

	public Cache(int size) {
		items = new Bookmark[size];
	}

	public void add(Bookmark item) {
		if (next < items.length) {

			items[next++] = item;
		}
	}

	public CacheIterator iterator() {
		return new MyCacheIterator();
	}

	private class MyCacheIterator implements CacheIterator {

		static double price = 2.3;
		private int i = 0;

		@Override
		public boolean hasNext() {
			return i < items.length;
		}

		@Override
		public Bookmark next() {
			return items[i++];
		}

	}

	public static void main(String... args) {
		Cache recommendedItems = new Cache(5);

		Bookmark item1 = new Bookmark(2000, "Use Final Liberally");
		Bookmark item2 = new Bookmark(2001, "How do I import a pre-existing Java project into Eclipse");
		Bookmark item3 = new Bookmark(2002, "Interface vs Abstract Class");
		Bookmark item4 = new Bookmark(2003, "NTO tutorial");
		Bookmark item5 = new Bookmark(2004, "Virtual Hosting");
		item1.setRating(3.5);
		item2.setRating(2.5);
		item3.setRating(6.5);
		item4.setRating(6.9);
		item5.setRating(1.5);

		recommendedItems.add(item1);
		recommendedItems.add(item2);
		recommendedItems.add(item3);
		recommendedItems.add(item4);
		recommendedItems.add(item5);

		CacheIterator iterator = recommendedItems.iterator();
		// hoac la ben duoi deu dung duoc
		// CacheIterator iterator = recommendedItems.new MyCacheIterator();

		while (iterator.hasNext()) {
			Bookmark bookmark = iterator.next();
			System.out.println(bookmark);
		}

		System.out.println("After sorting anonymous class....");
		Arrays.sort(recommendedItems.items, RATING_COMPARATOR);
		iterator = recommendedItems.iterator();
		while (iterator.hasNext()) {
			Bookmark bookmark = iterator.next();
			System.out.println(bookmark);
		}

		// Lambdas

		Arrays.sort(recommendedItems.items, (o1, o2) ->
			o1.getTitle().length() - o2.getTitle().length()
		);
		System.out.println("\nSorted by length (using lambda) ...");
		iterator = recommendedItems.iterator();

		while (iterator.hasNext()) {
			System.out.println(iterator.next().getTitle());

		}

		// go(() -> {System.out.println("\nLambda");});
	}

	private static int global = 0;
	private static void go(Test test) {
		test.apply();

		int count = 0;

		for (int i = 0; i < 5; i++) {
			new Thread(() -> System.out.println(count)).start();
			new Thread(() -> System.out.println(global++)).start();
		}

		new Thread(
				   new Runnable() {
					   public void run() {
					   }
				   }
);
	}




}

abstract class Test {
	abstract void apply();
}
