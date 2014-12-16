package com.gemt.grab.ws;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hello")
public class Hello {
	@GET
	@Path("/there/{f}/{m}")  
	@Produces(MediaType.TEXT_PLAIN)
	public String sayPlainTextHello(@PathParam("f") String f, @PathParam("m") String m){
		return "Hello " + f + " " + m;
	}

	@GET
	@Path("/here")
	@Produces(MediaType.TEXT_XML)
	public String sayXMLHello() {
		return "<?xml version=\"1.0\"?>" + "<hello> Hello Jersey" + "</hello>";
	}


	@GET
	@Path("/now")
	@Produces(MediaType.TEXT_HTML)
	public String sayHtmlHello() {
		return "<html> " + "<title>" + "Hello Jersey" + "</title>"
				+ "<body><h1>" + "Hello Jersey" + "</body></h1>" + "</html> ";
	}
}
