package com.booksaloha.managers;

import com.booksaloha.entities.Book;

import java.util.ArrayList;
import java.util.Collection;

public class BookManager {
	private static final BookManager instance = new BookManager();

	private BookManager() {}

	public static BookManager getInstance() {
		return instance;
	}

	public Collection<Book> getMyBooks() {
		Collection<Book> myBooks = new ArrayList<>();

		// first book
		Book book = new Book();
		book.setImageUrl("http://photo.goodreads.com/books/1170846378m/73968.jpg");
		book.setAuthor("Erich Segal");
		book.setRating(3.44);

		myBooks.add(book);
		// second book
		book = new Book();
		book.setImageUrl("http://ecx.images-amazon.com/images/I/21WBe6pNO5L._SX106_.jpg");
		book.setAuthor("Lillian Eichler Watson");
		book.setRating(5.0);

		myBooks.add(book);

		return myBooks;
	}
}