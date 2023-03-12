package com.semanticsquare.collection.demo;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.*;

public class SetDemo {
	private static void hashSetDemo() {
		/*
		 * Set<String> set1 = new HashSet<>(); set1.add("a"); set1.add("b");
		 * set1.add("a");
		 * 
		 * System.out.println("set1: " + set1);
		 */
		Book book1 = new Book("Walden", "Henry Thoreau", 1854);
		Book book2 = new Book("Arcane", "Riot", 2019);
		Set<Book> set2 = new LinkedHashSet<>();
		set2.add(book1);
		set2.add(book2);
		System.out.println("book1.hashCode: " + book1.hashCode());
		System.out.println("book2.hashCode: " + book2.hashCode());
		System.out.println("set2: " + set2);

	}

	private static void linkedHashSetDemo() {
		Set<String> hashSet = new HashSet<>();
		hashSet.add("Raj");
		hashSet.add("John");
		hashSet.add("Anita");
		System.out.println("hashSet: " + hashSet);

		Set<String> linkedHashSet = new LinkedHashSet<>();
		linkedHashSet.add("Raj");
		linkedHashSet.add("John");
		linkedHashSet.add("Anita");
		System.out.println("linkedHashSet: " + linkedHashSet);
	}

	private static void treeSetDemo2() {
		NavigableSet<Integer> set = new TreeSet<>();
		set.add(5);
		set.add(74);
		set.add(23);
		set.add(89);

		System.out.println("Lower: " + set.lower(74));
		System.out.println("floor: " + set.floor(74));
		System.out.println("ceiling: " + set.ceiling(74));
		System.out.println("higher: " + set.higher(74));

		System.out.println("first: " + set.first());
		System.out.println("last: " + set.last());

		System.out.println("set: " + set);

		NavigableSet<Integer> set2 = set.descendingSet();
		System.out.println("set2: " + set2);

		NavigableSet<Integer> headSet = set.headSet(74, false);
		System.out.println("headSet: " + headSet);

		NavigableSet<Integer> headSetWithInclude = set.headSet(74, true);

		System.out.println("headSetWithInclusive: " + headSetWithInclude);

		System.out.println("set: " + set);
		// headSetWithInclude.add(30);
		set.add(24);
		System.out.println("headSet: " + headSetWithInclude);
		System.out.println("set: " + set);

		SortedSet<Integer> sortedSet = set.subSet(3, 50);
		System.out.println("sortedSet: " + sortedSet);
		sortedSet.add(6);
		System.out.println("sortedSet: " + sortedSet);

	}

	public static void main(String[] args) {
		// hashSetDemo();
		// linkedHashSetDemo();
		// treeSetDemo();
		treeSetDemo2();
	}

	private static void treeSetDemo() {
		Book book1 = new Book("Harry Potter", "J.K.Rowling", 2015);
		Book book2 = new Book("Harry Potter", "J.K.Rowling", 2015);
		Book book3 = new Book("Walden", "Henry David Thoreau", 1854);
		Book book4 = new Book("Effective Java", "Joshua Bloch", 2008);

		Set<Book> books = new TreeSet<>(new YearComparator());
		books.add(book1);
		books.add(book2);
		books.add(book3);
		books.add(book4);

		for (Book book : books) {
			System.out.println(book);
		}

	}
}

class Book /* implements Comparable */ {
	private String title;
	private String author;
	private int year;

	// @Override
	// public int compareTo(Object o) {
	// return title.compareTo(((Book)o).getTitle());
	// }

	public Book(String title, String author, int year) {
		this.title = title;
		this.author = author;
		this.year = year;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Book{");
		sb.append("title='").append(title).append('\'');
		sb.append(", author='").append(author).append('\'');
		sb.append(", year=").append(year);
		sb.append('}');
		return sb.toString();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;

	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (o == null || getClass() != o.getClass())
			return false;

		Book book = (Book) o;

		return new EqualsBuilder().append(year, book.year).append(title, book.title).append(author, book.author)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(title).append(author).append(year).toHashCode();
	}

}

class TitleComparator implements Comparator {

	@Override
	public int compare(Object o1, Object o2) {
		return ((Book) o1).getTitle().compareTo(((Book) o2).getTitle());
	}

}

class YearComparator implements Comparator {

	@Override
	public int compare(Object o1, Object o2) {
		return ((Book) o1).getYear() - ((Book) o2).getYear();
	}
}
