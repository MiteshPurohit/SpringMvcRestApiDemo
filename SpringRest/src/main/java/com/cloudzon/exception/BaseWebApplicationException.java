package com.cloudzon.exception;

public abstract class BaseWebApplicationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int status;
	private final String errorMessage;
	private final String errorCode;
	private final String developerMessage;

	public BaseWebApplicationException(int httpStatus, String errorCode,
			String errorMessage, String developerMessage) {
		this.status = httpStatus;
		this.errorMessage = errorMessage;
		this.errorCode = errorCode;
		this.developerMessage = developerMessage;
	}

	public int getStatus() {
		return status;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getDeveloperMessage() {
		return developerMessage;
	}

}
