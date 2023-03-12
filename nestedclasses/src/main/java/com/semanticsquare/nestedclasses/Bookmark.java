package com.semanticsquare.nestedclasses;

import java.io.Serializable;
import java.util.Comparator;

public class Bookmark {

	private long id;
	private String title;
	private double rating;

	public static class ComparatorList implements Serializable {
		public static class RatingComparator implements Comparator<Bookmark>, Serializable {

			@Override
			public int compare(Bookmark o1, Bookmark o2) {
				return o1.getRating() - o2.getRating() > 0 ? 1 : -1;
			}

		}

		public static class TitleComparator implements Comparator<Bookmark>, Serializable {

			@Override
			public int compare(Bookmark o1, Bookmark o2) {
				return o1.getTitle().compareTo(o2.getTitle());
			}

		}
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Bookmark() {
	}

	public Bookmark(long id, String title) {
		this.id = id;
		this.title = title;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	@Override
	public String toString() {
		return "Bookmark [id=" + id + ", rating=" + rating + ", title=" + title + "]";
	}
}
