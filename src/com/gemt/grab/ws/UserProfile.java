package com.gemt.grab.ws;


import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.gemt.grab.beans.ResponseBean;
import com.gemt.grab.beans.UserProfileBean;
import com.gemt.grab.dao.UserProfileDAO;

@Path("/user")
public class UserProfile {
	
	
	@GET @Path("{userid}")
	@Produces({ MediaType.APPLICATION_JSON })
	public UserProfileBean getUserProfile(@PathParam("userid") int userid) {		
		return UserProfileDAO.getUserProfile(userid);		
	}
	
	@PUT @Path("{username}")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public ResponseBean update(UserProfileBean user) {
		/*ObjectMapper mapper = new ObjectMapper();
		try {
			System.out.println(mapper.writeValueAsString(user));
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		return UserProfileDAO.updateUser(user);
	}
}
