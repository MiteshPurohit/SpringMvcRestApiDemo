package com.cloudzon.dto;

import java.io.Serializable;
import java.util.Date;

import com.cloudzon.model.User;

public class AccessTokenModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date lastAccess;
	private User user;

	public AccessTokenModel() {

	}

	public AccessTokenModel(Date lastAccess, User user) {
		this.lastAccess = lastAccess;
		this.user = user;
	}

	public Date getLastAccess() {
		return lastAccess;
	}

	public void setLastAccess(Date lastAccess) {
		this.lastAccess = lastAccess;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
