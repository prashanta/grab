package com.gemt.grab.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gemt.grab.beans.FolderBean;
import com.gemt.grab.utility.ConnectionHelper;

public class FolderDAO {
	public static List<FolderBean> getFolders(int userId){
		List<FolderBean> folders = new ArrayList<FolderBean>();
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
}
