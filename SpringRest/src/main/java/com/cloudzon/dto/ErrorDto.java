package com.cloudzon.dto;

public class ErrorDto {

	private String errorMessage;
	private String errorCode;
	private String developerMessage;

	public ErrorDto() {

	}

	public ErrorDto(String errorMessage, String errorCode,
			String developerMessage) {
		this.errorMessage = errorMessage;
		this.errorCode = errorCode;
		this.developerMessage = developerMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getDeveloperMessage() {
		return developerMessage;
	}

	public void setDeveloperMessage(String developerMessage) {
		this.developerMessage = developerMessage;
	}

}
