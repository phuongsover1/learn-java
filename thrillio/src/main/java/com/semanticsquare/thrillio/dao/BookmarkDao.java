package com.semanticsquare.thrillio.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.semanticsquare.thrillio.DataStore;
import com.semanticsquare.thrillio.entities.Book;
import com.semanticsquare.thrillio.entities.Bookmark;
import com.semanticsquare.thrillio.entities.Movie;
import com.semanticsquare.thrillio.entities.UserBookmark;
import com.semanticsquare.thrillio.entities.WebLink;

public class BookmarkDao {
	public List<List<Bookmark>> getBookmark() {
		return DataStore.getBookmarks();
	}

	public void saveUserBookmark(UserBookmark userBookmark) {
		try(Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/jid_thrillio?useSSL=false","root", "Phuongsover1");
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

		try(Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/jid_thrillio?useSSL=false", "root", "Phuongsover1");
			Statement stmt = conn.createStatement()) {

			String sql = "update " + tableToUpdate + " set kid_friendly_status = " + kidFriendlyStatus +", kid_friendly_marked_by ="
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

		try(Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/jid_thrillio?useSSL=false", "root", "Phuongsover1");
			Statement stmt = conn.createStatement()) {

			String sql = "update " + tableToUpdate + " set shared_by = " + userId
				+ " where id =" + bookmark.getId();
			stmt.executeUpdate(sql);
		}catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
