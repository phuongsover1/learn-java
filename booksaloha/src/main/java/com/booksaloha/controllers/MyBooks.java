package com.booksaloha.controllers;

import com.booksaloha.entities.Book;
import com.booksaloha.managers.BookManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

@WebServlet("/mybooks")
public class MyBooks extends HttpServlet {
	private static final long serialVersionUID = 1L;

/*
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");

		PrintWriter out = resp.getWriter();
		out.println("<html>");
		out.println("<head>"+
				"<meta charset=\"ISO-8859-1\">"+
				"<title>Books</title>"+
				"</head>"+
				"<body style=\"font-family:Arial;font-size:20px;\">"+
				"<div style=\"height:65px;align: center;background: #DB5227;font-family: Arial;color: white;\">"+
				"<br><b>"+
				"<a href=\"\" style=\"font-family:garamond;font-size:34px;margin:0 0 0 10px;color:white;text-decoration: none;\">Books<i>Aloha!</i></a></b>"+
				"</div>"+
				"<br><br>"+
				"<table>"+
				"<tr>"+
				"<td>"+
				"<img src=\"http://photo.goodreads.com/books/1170846378m/73968.jpg\">"+
				"</td>"+

				"<td style=\"color:gray;\">"+
				" By <span style=\"color: #B13100;\">Erich Segal</span>"+
				"<br><br>"+
				"Rating: <span style=\"color: #B13100;\">3.44</span>"+
				"</td>"+
				"</tr>"+
				"<tr>"+
				"<td>&nbsp;</td>"+
				"</tr>"+
				"<tr>"+
				"<td>"+
				" <img src=\"http://ecx.images-amazon.com/images/I/21WBe6pNO5L._SX106_.jpg\" />"+
				"</td>"+
				"<td style=\"color:gray;\">"+
				" By <span style=\"color:#B13100;\">Lillian Eichler Watson</span>"+
				" <br><br>"+
				"Rating: <span style=\"color: #B13100;\">5.0</span>"+
				"</td>"+
				"</tr>"+


				"</table>"+
				"</body>");
		out.println("</html>");
	}
*/

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 1.Get data from model
		 Collection<Book> myBooks = BookManager.getInstance().getMyBooks();
		 req.setAttribute("myBooks", myBooks);
		System.out.println(myBooks);

		// 2.Forwarding to View
		RequestDispatcher dispatcher = req.getRequestDispatcher("MyBooks.jsp");
		dispatcher.forward(req,resp);
	}
}