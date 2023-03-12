package com.semanticsquare.thrillio.controllers;

import com.semanticsquare.thrillio.entities.User;
import com.semanticsquare.thrillio.managers.UserManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = {"/auth", "/auth/logout"})
public class AuthController extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (!request.getServletPath().contains("logout")) {
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			System.out.println("email: " + email);
			System.out.println("password: " + password);
			long userId = UserManager.getInstance().authenticate(email, password);
			if (userId != -1) {
				HttpSession session = request.getSession();
				session.setAttribute("userId", userId);
				request.getRequestDispatcher("bookmark/mybooks").forward(request,response);
			} else
				request.getRequestDispatcher("/login.jsp").forward(request,response);
		} else {
			request.getSession().invalidate();
			request.getRequestDispatcher("/login.jsp").forward(request,response);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req,resp);
	}
}