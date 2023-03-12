import java.util.Arrays;

public class Instructor {
	public long id;
	public String name;
	public String title;
	public String department;
	public Book[] books;

	// this() vs this reference. this() is the first statement!Book
	// Cach 1:
	// public Instructor(long id, String name, String title, String department,
	// Book[] books) {
	// this(id, name, title, department);
	// this.books = books;
	// }

	// public Instructor(long id, String name, String title, String department) {
	// this(id, name, title);
	// this.department = department;
	// }

	// public Instructor(long id, String name, String title) {
	// this(id, name);
	// this.title = title;
	// }

	// public Instructor(long id, String name) {
	// this.id = id;
	// this.name = name;
	// }

	// Cach 2:
	// Primary Constructor
	public Instructor(long id, String name, String title, String department, Book[] books) {
		this.id = id;
		this.name = name;
		this.title = title;
		this.department = department;
		this.books = books;
	}

	public Instructor(long id, String name) {
		this(id, name, null, null, null);
	}

	public Instructor(long id, String name, String title) {
		this(id, name, title, null, null);
	}

	public Instructor(long id, String name, String title, String department) {
		this(id, name, title, department, null);
	}

	// #1. No index hard-coding (return books[2].getTitle())

	public String getMostRecentBookTitle() {
		return books[books.length - 1].getTitle();
	}

	public Book updateBook(int index, String title) {
		Book oldBook = new Book(books[index].getTitle());
		books[index].setTitle(title);
		return oldBook;
	}

	// Common Mistakes:
	// - #2: Not using temp variable
	// - #3: oldBook & books[index] reference same book object
	// public Book updateBook(int index, String title) {
	// Book oldBook = books[index];
	// books[index].setTitle(title);
	// return oldBook;
	// }

	// Version 2 of above method
	// public Book updateBook(int index, String title) [
	// Book oldBook = new Book(); // Requires adding a no-arg constructor in Book
	// oldBook.setTitle(books[index].getTitle());
	// books[index].setTitle(title);
	// return oldBook;
	// }

	public Book updateBook(int index, Book book) {
		Book oldBook = books[index];
		books[index] = book;
		return oldBook;
	}

	@Override
	public String toString() {
		return "Instructor [books=" + Arrays.toString(books) + ", department=" + department + ", id=" + id + ", name="
				+ name + ", title=" + title + "]";
	}

}
