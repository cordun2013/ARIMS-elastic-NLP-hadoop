package com.arims.classify;

import java.io.Serializable;

import org.springframework.stereotype.Component;


@Component
public class Webstate implements Serializable{
	private static final long serialVersionUID = 8274434126166707083L;
	private String user;
	
	public Webstate(){
		System.out.println("Inside construction");
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	
}
