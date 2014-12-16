package com.gemt.grab.ws;


import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;



import com.gemt.grab.beans.FolderBean;
import com.gemt.grab.dao.FolderDAO;

@Path("/folder")
public class Folder {
	@GET @Path("{uid}")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<FolderBean> getFolders(@PathParam("uid") int uid) {
		List<FolderBean> folders = FolderDAO.getFolders(uid); 
		return folders;
	}	
}
