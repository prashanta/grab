package com.gemt.grab.utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;


public class Utility {
	public static int validatePath(String username, String password, String path){
		String user = username + ":" + password;
		NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(user);
		String finalPath = path.replaceAll("\\\\", "/");
		finalPath = "smb:" + (finalPath.indexOf("//")  == -1? ("//" + finalPath) : finalPath);
		finalPath = finalPath.lastIndexOf("/") == finalPath.length()-1? finalPath : finalPath + "/";
		try {
			SmbFile sFile = new SmbFile(finalPath, auth);			
			if(sFile.listFiles().length > -1 && sFile.canWrite()){								
				return 1;							
			}
			else{				
				return 0;
			}				
		} catch (Exception e) {				
			e.printStackTrace();
			return -1;
		}		
	}
	
	public static void setUsernameCookie(String username, HttpServletResponse response){
		setCookie("user", username, response);	
	}
	
	public static void setPasswordCookie(String password, HttpServletResponse response){
		setCookie("token", password, response);	
	}
	
	public static void setPathCookie(String path, HttpServletResponse response){
		setCookie("folderpath", path, response);	
	}
	
	public static void setCookie(String key, String value, HttpServletResponse response){
		Cookie c = new Cookie(key, value);
		c.setMaxAge(365 * 60 * 60 * 24);
		response.addCookie(c);
	}
	
	public static String getUsernameCookie(HttpServletRequest request){
		return getCookie("user", request);
	}
	
	public static String getPasswordCookie(HttpServletRequest request){
		return getCookie("token", request);
	}
	
	public static String getPathCookie(HttpServletRequest request){
		return getCookie("folderpath", request);
	}
	
	public static String getCookie(String key, HttpServletRequest request){
		Cookie[] cookies = request.getCookies();		
    	if (cookies != null){    		
    		for(int i = 0; i < cookies.length; i++) {    			    			 	    		
    			if(cookies[i].getName().equals(key)){
    				String temp = cookies[i].getValue();    				
    				return temp;    			    
    			}
    		}
    		return "";
    	}
    	else
    		return "";
	}
	
	public static String getPathFromFile(String username){
		FileInputStream fis = null;
		try {
			Properties properties = new Properties();
			fis = new FileInputStream("C:/grab/users.properties");
			properties.load(fis);
			if(properties.getProperty(username) != null){
				String path = properties.getProperty(username);				
				fis.close();
				return path;				
			}								
			else
				return "";
		} catch (Exception e) {			
			e.printStackTrace();
			return "";
		}
		finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}			 
		}		
	}
}

