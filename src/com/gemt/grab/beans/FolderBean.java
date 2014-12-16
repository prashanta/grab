package com.gemt.grab.beans;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FolderBean {
	
	int folderId;
	String name = "";
	String path = "";
	int imageResize = -1;
	
	public int getFolderId() {
		return folderId;
	}
	public void setFolderId(int folderId) {
		this.folderId = folderId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public int getImageResize() {
		return imageResize;
	}
	public void setImageResize(int imageResize) {
		this.imageResize = imageResize;
	}
}
