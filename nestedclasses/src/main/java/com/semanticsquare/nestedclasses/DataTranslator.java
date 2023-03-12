package com.semanticsquare.nestedclasses;

import java.io.StringWriter;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.StaxDriver;

class DataTranslator {

	public static String getBookAsXML(int id, String title, double rating, int fbLikesCount, int tweetCount) {

		class Book {
			private int id;
			private String title;
			private double rating;
			private int fbLikesCount;
			private int tweetCount;

			public Book(int id, String title, double rating, int fbLikesCount, int tweetCount) {
				this.id = id;
				this.title = title;
				this.rating = rating;
				this.fbLikesCount = fbLikesCount;
				this.tweetCount = tweetCount;
			}

		}
		Book book = new Book(id, title, rating, fbLikesCount, tweetCount);

		XStream xstream = new XStream(new StaxDriver());
		xstream.alias("book", Book.class);
		StringWriter writer = new StringWriter();
		xstream.marshal(book, new PrettyPrintWriter(writer));
		// asasasasasas
		return writer.toString();
	}

	public static void main(String... args) {
		System.out.println(getBookAsXML(2002, "Interface and Abstract classes", 5.0, 5, 6));
	}

}
