package com.semanticsquare.thrillio.dao;

import java.sql.*;
import java.util.List;

import com.semanticsquare.thrillio.DataStore;
import com.semanticsquare.thrillio.entities.User;

public class UserDao {
	public List<User> getUsers() {
		return DataStore.getUsers();
	}

	public User getUser(long userId) {
		return DataStore.getUser(userId);
	}

	public long authenticate(String email, String encodePassword) {
		System.out.println("encodePassword: " + encodePassword);
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try(Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/jid_thrillio?useSSL=false", "root", "Phuongsover1");
		    Statement stmt = conn.createStatement()) {
			String sql = "SELECT id FROM User WHERE email ='" + email + "' AND password='" + encodePassword +"'";

			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				return rs.getLong("id");
			}

		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
		return -1;
	}

}