package com.gemt.grab.localdao;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import com.gemt.grab.beans.FolderBean;
import com.gemt.grab.beans.ResponseBean;
import com.gemt.grab.beans.UserProfileBean;
import com.gemt.grab.utility.ConnectionHelper;

public class UserProfileDAO {
	
	public static UserProfileBean getUserProfile(int userid){				
		UserProfileBean user = null;
		Properties prop = new Properties();
		InputStream input = null;
		
		try {
			input = new FileInputStream("D:\\grab-config.properties");
			// load a properties file
			prop.load(input);

			user = new UserProfileBean();
			user.setUsername("user1");
			user.setUserid(1);				
			user.setCurrentFolder(Integer.parseInt(prop.getProperty("current_folder")));
			user.setUploadImmediately(Integer.parseInt(prop.getProperty("immediate_upload")));
			user.setPrinter("");
			
			ArrayList<FolderBean> folders = new ArrayList<FolderBean>();
			FolderBean folder = new FolderBean();
			folder.setFolderId(Integer.parseInt(prop.getProperty("folder1_id")));
			folder.setName(prop.getProperty("folder1_name"));
			folder.setPath(prop.getProperty("folder1_path"));
			folder.setImageResize(Integer.parseInt(prop.getProperty("image1_resize")));
			folders.add(folder);
			
			folder = new FolderBean();
			folder.setFolderId(Integer.parseInt(prop.getProperty("folder2_id")));
			folder.setName(prop.getProperty("folder2_name"));
			folder.setPath(prop.getProperty("folder2_path"));
			folder.setImageResize(Integer.parseInt(prop.getProperty("image2_resize")));
			folders.add(folder);
			
			folder = new FolderBean();
			folder.setFolderId(Integer.parseInt(prop.getProperty("folder3_id")));
			folder.setName(prop.getProperty("folder3_name"));
			folder.setPath(prop.getProperty("folder3_path"));
			folder.setImageResize(Integer.parseInt(prop.getProperty("image3_resize")));
			folders.add(folder);
			
			user.setFolders(folders);
			
			
        } catch (IOException ex) {
    		ex.printStackTrace();
    	}finally {
    		if (input != null) {
    			try {
    				input.close();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    		}
		}
		System.out.println(user.getCurrentFolder());
		System.out.println(user.getUploadImmediately());
		System.out.println(user.getFolders().get(0).getImageResize());
		return user;
	}
	
	public static ResponseBean updateUser(UserProfileBean user) throws SQLException{
		ResponseBean response = new ResponseBean();				
		response = updateProfile(user);
		if(response.getId() > -1)
			response = updateFolders(user);
		return response;		
	}
	
	public static ResponseBean updateProfile(UserProfileBean user) throws SQLException{
		ResponseBean response = new ResponseBean();
		Properties prop = new Properties();
		OutputStream output = null;
		Connection c = null;
		try {
			c = ConnectionHelper.getConnection(); 
			output = new FileOutputStream("D:\\grab-config.properties");

			// set the properties value
			prop.setProperty("current_folder", String.valueOf(user.getCurrentFolder()));
			prop.setProperty("immediate_upload", String.valueOf(user.getUploadImmediately()));
			
			int i = 0;
			
			for(FolderBean folder : user.getFolders()){
        		String sql = "UPDATE folders SET folder_name = ?, folder_path = ?, image_resize = ? WHERE folder_id = ? AND user_id = ?" ;
    			PreparedStatement statement = c.prepareStatement(sql);
    			statement.setString(1, folder.getName());			
    			statement.setString(2, folder.getPath());			
    			statement.setInt(3, folder.getImageResize());			
    			statement.setInt(4, folder.getFolderId());			
    			statement.setInt(5, user.getUserid());            	
    			statement.executeUpdate();
    			response.setId(0);        	
        	}
			
			prop.setProperty("folder1_id", String.valueOf(user.getCurrentFolder()));
			prop.setProperty("folder1_name", String.valueOf(user.getCurrentFolder()));
			prop.setProperty("folder1_path", String.valueOf(user.getCurrentFolder()));
			prop.setProperty("image1_resize", String.valueOf(user.getCurrentFolder()));
			
			// save properties to project root folder
			prop.store(output, null);

		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		response.setId(0);
		return response;
	}
	
	public static ResponseBean updateFolders(UserProfileBean user){
		ResponseBean response = new ResponseBean();
		
		return response;
	}
	
	public static FolderBean getCurrentFolder(int userId, int folderIndex){
		FolderBean folder = new FolderBean();
		
		Properties prop = new Properties();
		InputStream input = null;
		
		try {
			input = new FileInputStream("D:\\grab-config.properties");
			prop.load(input);
			
			folder.setName(prop.getProperty("folder"+folderIndex+"_name"));
			folder.setPath(prop.getProperty("folder"+folderIndex+"_path"));
			folder.setImageResize(Integer.parseInt(prop.getProperty("image"+folderIndex+"_resize")));	
			
        } catch (IOException ex) {
    		ex.printStackTrace();
    	}finally {
    		if (input != null) {
    			try {
    				input.close();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    		}
		}
		return folder;
	}			
}
