package com.booksaloha.entities;

public class Book {
	private String imageUrl;
	private String author;
	private double rating;

	@Override
	public String toString() {
		return "Book{" +
				"imageUrl='" + imageUrl + '\'' +
				", author='" + author + '\'' +
				", rating=" + rating +
				'}';
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}
}