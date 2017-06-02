package com.cloudzon.exception;

public class AuthenticationException extends BaseWebApplicationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AuthenticationException() {
		super(401, "40101", "The username or password were incorrect",
				"Authentication Error. The username or password were incorrect");
	}

	public AuthenticationException(String errorMessage, String developerMessage) {
		super(401, "40101", errorMessage, developerMessage);
	}
}
