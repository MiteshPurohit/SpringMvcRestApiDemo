package com.cloudzon.exception;

public class AuthorizationException extends BaseWebApplicationException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AuthorizationException(String applicationMessage) {
        super(401, "40102", "Not authorized", applicationMessage);
    }

}
