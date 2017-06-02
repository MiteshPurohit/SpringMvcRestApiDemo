package com.cloudzon.dto;

import java.io.Serializable;

public class AccessTokenContainer implements Serializable {

	private static final long serialVersionUID = 1L;
	private String access_token;
	private String expires_in;
	private String firstName;
	private String lastName;
	private String email;

	public AccessTokenContainer() {

	}

	public AccessTokenContainer(String access_token, String expires_in,
			String firstName, String lastName, String email) {
		super();
		this.access_token = access_token;
		this.expires_in = expires_in;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
