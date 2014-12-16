package com.gemt.grab.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Logout
 */
@WebServlet("/Logout")
public class Logout extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Logout() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.getSession().setAttribute("uid", null);
		request.getSession().setAttribute("uname", null);
		request.getSession().invalidate();
		
		Cookie c = new Cookie("uid", "");
		c.setMaxAge(365 * 60 * 60 * 24);
		response.addCookie(c);
		response.sendRedirect("index.jsp");
	}
}
