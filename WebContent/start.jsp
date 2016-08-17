<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.gemt.grab.utility.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>GRAB</title>
<link rel="stylesheet" href="resources/libs/bootstrap-3.1.1/css/bootstrap.css">
<link rel="stylesheet" href="resources/styles/gemt.css">
<link rel="stylesheet" href="resources/styles/body.css">
</head>
<body>
	<%
		// Check of session has already been started
		// UNDO String uid = Utility.getCookie("uid", request);
	
		// UNDO if (!uid.isEmpty()){		
			
		//if ((session.getAttribute("uname") != null) && (session.getAttribute("uname") != "")) {
	%>
	<div id="loadingDiv">
		<img style="margin-left: 50%; margin-top: 19%;" src="resources/images/ajax-loader.gif">
	</div>
	<div id="wrap">		
			<section id="header"></section>
			<section id="body"></section>			
			<div class="modal fade" id="modal_setting" tabindex="-1" role="dialog" aria-hidden="true"></div>		
		<div id="push"></div>
	</div>
	<div id="footer">
      <div>
        <a href='Logout'><button type="button" class="btn btn-default" style='margin-top: 8px; margin-left: 10px;'>Log out</button></a>
      </div>
    </div>
	
	
	<script data-main="resources/js/main" src="resources/libs/require-2.1.15.js"></script>
	<%		
	// UNDO }
	// UNDO else
			// UNDO response.sendRedirect("index.jsp");
	%>
</body>
</html>