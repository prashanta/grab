package com.gemt.grab.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gemt.grab.dao.UserProfileDAO;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		String username = request.getParameter("uname");
		String pass = request.getParameter("pass");
		PrintWriter out = response.getWriter();
		try {
		    Thread.sleep(2000);                 //1000 milliseconds is one second.
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
		response.setContentType("application/json");
		int userId = UserProfileDAO.logIn(username, pass); 
		if( userId > -1){
			request.getSession().setAttribute("uname", username);
			request.getSession().setAttribute("uid", userId);
			
			// Set cookie to remember user even after session has expired
			Cookie c = new Cookie("uid", ""+userId);
			c.setMaxAge(365 * 60 * 60 * 24);
			response.addCookie(c);
		    out.print("1");
		}
		else
			out.print("0");
	}
}
