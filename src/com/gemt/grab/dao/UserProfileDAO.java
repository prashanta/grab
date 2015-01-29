package com.gemt.grab.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.gemt.grab.beans.FolderBean;
import com.gemt.grab.beans.ResponseBean;
import com.gemt.grab.beans.UserProfileBean;
import com.gemt.grab.utility.ConnectionHelper;
import com.gemt.grab.utility.SmbUtility;
import com.mysql.jdbc.Statement;

public class UserProfileDAO {
	public static ResponseBean registerNewUser(String username, String password){
		ResponseBean resp = new ResponseBean();
		resp.setId(0);
        Connection c = null;
    	String sql = "SELECT user_id FROM `users` WHERE user_name = ?";
        try {
            c = ConnectionHelper.getConnection();            
            PreparedStatement statement = c.prepareStatement(sql);			
			statement.setString(1, username);			
			ResultSet rs = statement.executeQuery();		
			
			if(rs.next()){	
				resp.setId(-1);
				resp.setMessage("User name already exist.");
			}		      
			else{
				System.out.println("Creating new user");
				sql = "INSERT INTO users (user_name, password, current_folder, upload_immediately) VALUES(?, ?, -1, 0)";
				statement = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				statement.setString(1, username);
				statement.setString(2, password);
				statement.executeUpdate();
	            rs = statement.getGeneratedKeys();
	            rs.next();
	            int id = rs.getInt(1);
	            
	            sql = "INSERT INTO folders (user_id, folder_name, folder_path, image_resize) VALUES (?, '', '', 0), (?, '', '', 0), (?, '', '', 0)";
	            statement = c.prepareStatement(sql);
				statement.setInt(1, id);
				statement.setInt(2, id);
				statement.setInt(3, id);
				statement.executeUpdate();
			}
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
        return resp;
    }
	
	public static int logIn(String username, String password){
        Connection c = null;
    	String sql = "SELECT user_id FROM `users` WHERE user_name = ? AND password = ?";
        try {
            c = ConnectionHelper.getConnection();            
            PreparedStatement statement = c.prepareStatement(sql);			
			statement.setString(1, username);
			statement.setString(2, password);
			ResultSet rs = statement.executeQuery();		
			
			if(rs.next()){
				int userId = rs.getInt("user_id");				
				return userId;				
			}		            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
        return -1;
    }
	
	public static String getPassword(int userId){
		String password = null;
		Connection c = null;
    	String sql = "SELECT password FROM users WHERE user_id = ?";
        try {
            c = ConnectionHelper.getConnection();            
            PreparedStatement statement = c.prepareStatement(sql);			
			statement.setInt(1, userId);
			ResultSet rs = statement.executeQuery();		
			
			if(rs.next()){
				password = rs.getString("password");				
			}		            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
        return password;
	}
	
	public static UserProfileBean getUserProfile(int userid){		
		UserProfileBean user = null;
		Connection c = null;
		String sql = "SELECT user_name, current_folder, upload_immediately, printer FROM users WHERE user_id = ?";
		try {
            c = ConnectionHelper.getConnection();            
            PreparedStatement statement = c.prepareStatement(sql);			
			statement.setInt(1, userid);			
			ResultSet rs = statement.executeQuery();					
			if(rs.next()){
				user = new UserProfileBean();
				user.setUsername(rs.getString("user_name"));
				user.setUserid(userid);				
				user.setCurrentFolder(rs.getInt("current_folder"));
				user.setUploadImmediately(rs.getInt("upload_immediately"));
				user.setPrinter(rs.getString("printer"));
				user.setFolders(getFolders(user.getUserid()));
			}		            			
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
		return user;
	}
	
	public static ArrayList<FolderBean> getFolders(int userId){
		ArrayList<FolderBean> folders = new ArrayList<FolderBean>();
		Connection c = null;
		String sql = "SELECT folder_id, folder_name, folder_path, image_resize FROM folders WHERE user_id = ?";
		try {
            c = ConnectionHelper.getConnection();            
            PreparedStatement statement = c.prepareStatement(sql);			
			statement.setInt(1, userId);			
			ResultSet rs = statement.executeQuery();					
			while(rs.next()){
				FolderBean folder = new FolderBean();
				folder.setFolderId(rs.getInt("folder_id"));
				folder.setName(rs.getString("folder_name"));
				folder.setPath(rs.getString("folder_path"));
				folder.setImageResize(rs.getInt("image_resize"));
				folders.add(folder);
			}		            			
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
		return folders;
	}
	
	public static ResponseBean updateUser(UserProfileBean user){
		ResponseBean response = new ResponseBean();		
		response = updateProfile(user);
		if(response.getId() > -1)
			response = updateFolders(user);
		return response;
		
	}
	
	public static ResponseBean updateProfile(UserProfileBean user){
		ResponseBean response = new ResponseBean();
		Connection c = null;
		try {
			c = ConnectionHelper.getConnection();
			String sql = "UPDATE users SET upload_immediately = ?, current_folder = ? WHERE user_id = ?" ;
			PreparedStatement statement = c.prepareStatement(sql);
			statement.setInt(1, user.getUploadImmediately());			
			statement.setInt(2, user.getCurrentFolder());			
			statement.setInt(3, user.getUserid());						
			statement.executeUpdate();
							                      
		} catch (SQLException e) {
			e.printStackTrace();
			//throw new RuntimeException(e);
			response.setId(-1);
			response.setMessage(e.getMessage());
			return response;
		} finally {
			ConnectionHelper.close(c);
		}		
		response.setId(0);
		return response;
	}
	
	public static ResponseBean updateFolders(UserProfileBean user){
		ResponseBean response = new ResponseBean();
		String password = getPassword(user.getUserid());
		Connection c = null;		
		try {			
            c = ConnectionHelper.getConnection();
            ArrayList<FolderBean> folders = user.getFolders();
            String message = "";
            // Validate paths first
            for(FolderBean folder : folders){
            	if(!folder.getName().isEmpty() && !folder.getPath().isEmpty()){	            	
	            	if(SmbUtility.validatePath(user.getUsername(), password, folder.getPath()) < 1){            		
	            		response.setId(-1);
	            		message += ("[" + folder.getPath() + "]<br>");            		
	            	}
            	}
            }
            if(response.getId() == -1){
            	response.setMessage("Invalid folder or no write permission for: <br>" + message);
            }
            else{
            	for(FolderBean folder : folders){
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.setId(-1);
			response.setMessage(e.getMessage());
			return response;
		} finally {
			ConnectionHelper.close(c);
		}
		return response;
	}
	
	public static int getCurrentFolderIndex(int userId){
		int folderIndex = -1;
		Connection c = null;
		String sql = "SELECT current_folder FROM users WHERE user_id=?";
		try {			
			c = ConnectionHelper.getConnection();
			PreparedStatement statement = c.prepareStatement(sql);
			statement.setInt(1, userId);			
			ResultSet rs = statement.executeQuery();		
			
			if(rs.next()){
				folderIndex = rs.getInt("current_folder");				
			}		   				                                  
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		} finally {
			ConnectionHelper.close(c);
		}
		return folderIndex;
	}
	
	public static FolderBean getCurrentFolder(int userId, int folderIndex){
		FolderBean folder = new FolderBean();
		Connection c = null;
		String sql = "SELECT folder_name, folder_path, image_resize FROM folders WHERE user_id=? ORDER BY folder_id LIMIT ?,1";
		try {
            c = ConnectionHelper.getConnection();
    		PreparedStatement statement = c.prepareStatement(sql);
    		statement.setInt(1, userId);
    		statement.setInt(2, folderIndex);			
    		ResultSet rs = statement.executeQuery();		
			
			if(rs.next()){
				folder.setName(rs.getString("folder_name"));				
				folder.setPath(rs.getString("folder_path"));				
				folder.setImageResize(rs.getInt("image_resize"));				
			}		   				                                  
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
		} finally {
			ConnectionHelper.close(c);
		}
		return folder;
	}			
}
