package com.gemt.grab.beans;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserProfileBean {
	
	String username = "";	
	int userid = -1;
	int currentFolder = 0;
	int uploadImmediately = 0;
	String printer = "";
	ArrayList<FolderBean> folders;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public int getCurrentFolder() {
		return currentFolder;
	}

	public void setCurrentFolder(int currentFolder) {
		this.currentFolder = currentFolder;
	}

	public int getUploadImmediately() {
		return uploadImmediately;
	}

	public void setUploadImmediately(int uploadImmediately) {
		this.uploadImmediately = uploadImmediately;
	}
	
	public String getPrinter() {
		return printer;
	}

	public void setPrinter(String printer) {
		this.printer = printer;
	}

	public ArrayList<FolderBean> getFolders() {
		return folders;
	}

	public void setFolders(ArrayList<FolderBean> folders) {
		this.folders = folders;
	}
}
