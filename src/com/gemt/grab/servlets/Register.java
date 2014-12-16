package com.gemt.grab.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import com.gemt.grab.beans.ResponseBean;
import com.gemt.grab.dao.UserProfileDAO;
import com.gemt.grab.utility.SmbUtility;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Register() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		String username = request.getParameter("uname");
		String pass = request.getParameter("pass");
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		ResponseBean resp = new ResponseBean();
		if(SmbUtility.validateUser(username, pass) == 1){			
			resp = UserProfileDAO.registerNewUser(username, pass);			
		}
		else{
			resp.setId(-1);
			resp.setMessage("Provided user credential not have permission to access remote folders.");			
		}
		ObjectMapper mapper = new ObjectMapper();
		try {
			out.print(mapper.writeValueAsString(resp));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
