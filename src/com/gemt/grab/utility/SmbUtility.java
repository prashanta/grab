package com.gemt.grab.utility;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;


public class SmbUtility {
	public static int validatePath(String username, String password, String path){
		String user = username + ":" + password;
		NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(user);
		String finalPath = path.replaceAll("\\\\", "/");
		finalPath = "smb:" + (finalPath.indexOf("//")  == -1? ("//" + finalPath) : finalPath);
		finalPath = finalPath.lastIndexOf("/") == finalPath.length()-1? finalPath : finalPath + "/";
		//System.out.println(user);
		//System.out.println(finalPath);
		try {
			SmbFile sFile = new SmbFile(finalPath, auth);			
			if(sFile.listFiles().length > -1 && sFile.canWrite()){
				System.out.println("Validating credential for folder [" + finalPath + "]... PASS");				
				return 1;							
			}
			else{				
				System.out.println("Validating credential for folder [" + finalPath + "]... FAIL");				
				return 0;
			}				
		} catch (Exception e) {
			System.out.println("Validating credential for folder [" + finalPath + "]... FAIL");				
			e.printStackTrace();
			return -1;
		}		
	}
	
	public static int validateUser(String username, String password){		
		String path  = "\\\\GEMT05S\\GEMT-Share\\Store";
		return validatePath(username, password, path);		
	}
}

