package com.gemt.grab.beans;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ResponseBean {
	
	int id;
	String message = "";
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
