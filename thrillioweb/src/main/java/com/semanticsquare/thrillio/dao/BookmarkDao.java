package com.semanticsquare.thrillio.dao;

import com.semanticsquare.thrillio.DataStore;
import com.semanticsquare.thrillio.constants.BookGenre;
import com.semanticsquare.thrillio.entities.*;
import com.semanticsquare.thrillio.managers.BookmarkManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BookmarkDao {
	public List<List<Bookmark>> getBookmark() {
		return DataStore.getBookmarks();
	}

	public void saveUserBookmark(UserBookmark userBookmark) {
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/jid_thrillio?useSSL=false", "root", "Phuongsover1");
		     Statement stmt = conn.createStatement()) {
			if (userBookmark.getBookmark() instanceof Book) {
				saveUserBook(userBookmark, stmt);
			} else if (userBookmark.getBookmark() instanceof Movie) {
				saveUserMovie(userBookmark, stmt);
			} else {
				saveUserWebLink(userBookmark, stmt);
			}

		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	private void saveUserWebLink(UserBookmark userBookmark, Statement stmt) {

		String query = "insert into User_WebLink (user_id, weblink_id) "
				+ "VALUES (" + userBookmark.getUser().getId() + ","
				+ userBookmark.getBookmark().getId() + ")";
		try {
			stmt.executeUpdate(query);
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	private void saveUserMovie(UserBookmark userBookmark, Statement stmt) {

		String query = "insert into User_Movie (user_id, movie_id) "
				+ "VALUES (" + userBookmark.getUser().getId() + ","
				+ userBookmark.getBookmark().getId() + ")";
		try {
			stmt.executeUpdate(query);
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	private void saveUserBook(UserBookmark userBookmark, Statement stmt) {
		String query = "insert into User_Book (user_id, book_id) "
				+ "VALUES (" + userBookmark.getUser().getId() + ","
				+ userBookmark.getBookmark().getId() + ")";
		try {
			stmt.executeUpdate(query);
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	public void updateKidFriendlyStatus(Bookmark bookmark) {
		int kidFriendlyStatus = bookmark.getKidFriendlyStatus().ordinal();
		long userId = bookmark.getKidFriendlyMarkedBy().getId();

		String tableToUpdate = "Book";
		if (bookmark instanceof Movie)
			tableToUpdate = "Movie";
		else if (bookmark instanceof WebLink)
			tableToUpdate = "WebLink";

		try (Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/jid_thrillio?useSSL=false", "root", "Phuongsover1");
		     Statement stmt = conn.createStatement()) {

			String sql = "update " + tableToUpdate + " set kid_friendly_status = " + kidFriendlyStatus + ", kid_friendly_marked_by ="
					+ userId + " where id = " + bookmark.getId();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void shareByInfo(Bookmark bookmark) {
		long userId = bookmark.getSharedBy().getId();

		String tableToUpdate = "Book";
		if (bookmark instanceof WebLink)
			tableToUpdate = "WebLink";

		try (Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/jid_thrillio?useSSL=false", "root", "Phuongsover1");
		     Statement stmt = conn.createStatement()) {

			String sql = "update " + tableToUpdate + " set shared_by = " + userId
					+ " where id =" + bookmark.getId();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public Collection<Bookmark> getBooks(boolean isBookmarked, long userId) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			// com.mysql.jdbc.Driver();
			// OR //
			// System.setProperty("jdbc.drivers", "com.mysql.jdbc.Driver");
			// OR
			// java.sql.DriverManager
			// //DriverManager.registerDriver(new com.mysql.jdbc.Driver());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		String sql = null;
		Collection<Bookmark> result = new ArrayList<>();
		if (!isBookmarked) {

			sql = "SELECT b.id, title, img_url, GROUP_CONCAT(a.name SEPARATOR ',') authors,"
					+ "publication_year, p.name , book_genre_id, amazon_rating, kid_friendly_status, kid_friendly_marked_by, shared_by "
					+ "FROM Book b, Author a, Book_Author ba, Publisher p "
					+ "WHERE b.id = ba.book_id AND a.id = ba.author_id AND p.id = b.publisher_id AND "
					+ "b.id NOT IN (SELECT ub.book_id FROM  User_Book ub WHERE ub.user_id =" + userId + ") "
					+ "GROUP BY b.id";
		} else {
			sql = "SELECT b.id, title, img_url, GROUP_CONCAT(a.name SEPARATOR ',') authors,"
					+ "publication_year, p.name, book_genre_id, amazon_rating, kid_friendly_status, kid_friendly_marked_by, shared_by "
					+ "FROM Book b, Author a, Book_Author ba, Publisher p "
					+ "WHERE b.id = ba.book_id AND a.id = ba.author_id AND p.id = b.publisher_id AND "
					+ "b.id IN (SELECT ub.book_id FROM  User_Book ub WHERE ub.user_id =" + userId + ") "
					+ "GROUP BY b.id";
		}


		try (Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/jid_thrillio?useSSL=false", "root", "Phuongsover1");
		     Statement stmt = conn.createStatement()) {
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {

				long id = rs.getLong("id");
				String title = rs.getString("title");
				String imgUrl = rs.getString("img_url");
				int publicationYear = rs.getInt("publication_year");
				String publisher = rs.getString("name");
				String[] authors = rs.getString("authors").split(",");
				int genre_id = rs.getInt("book_genre_id");
				BookGenre genre = BookGenre.values()[genre_id];
				double amazonRating = rs.getDouble("amazon_rating");

				Bookmark bookmark = BookmarkManager.getInstance().createBook(id, title, imgUrl, publicationYear, publisher, authors, genre, amazonRating);
				result.add(bookmark);
				System.out.println(bookmark);
			}

		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
		return result;
	}

	public Bookmark getBook(long bookId) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			// com.mysql.jdbc.Driver();
			// OR //
			// System.setProperty("jdbc.drivers", "com.mysql.jdbc.Driver");
			// OR
			// java.sql.DriverManager
			// //DriverManager.registerDriver(new com.mysql.jdbc.Driver());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		String sql = "SELECT b.id, title, img_url, GROUP_CONCAT(a.name SEPARATOR ',') authors,"
				+ "publication_year, p.name , book_genre_id, amazon_rating, kid_friendly_status, kid_friendly_marked_by, shared_by "
				+ "FROM Book b, Author a, Book_Author ba, Publisher p "
				+ "WHERE b.id = " + bookId + " AND b.id = ba.book_id AND a.id = ba.author_id AND p.id = b.publisher_id ";


		try (Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/jid_thrillio?useSSL=false", "root", "Phuongsover1");
		     Statement stmt = conn.createStatement()) {
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {

				long id = rs.getLong("id");
				String title = rs.getString("title");
				String imgUrl = rs.getString("img_url");
				int publicationYear = rs.getInt("publication_year");
				String publisher = rs.getString("name");
				String[] authors = rs.getString("authors").split(",");
				int genre_id = rs.getInt("book_genre_id");
				BookGenre genre = BookGenre.values()[genre_id];
				double amazonRating = rs.getDouble("amazon_rating");

				return BookmarkManager.getInstance().createBook(id, title, imgUrl, publicationYear, publisher, authors, genre, amazonRating);
			}

		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
		return BookmarkManager.getInstance().getDefaultBook();
	}
}