package com.cloudzon.exception;

public class DuplicateException extends BaseWebApplicationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DuplicateException(DuplicateFound duplicateFound) {
		super(409, duplicateFound.getStatusCode(), duplicateFound
				.getErrorMessage(), duplicateFound.getDeveloperMessage());
	}

	public enum DuplicateFound {
		DuplicateUserFound("40901", "User already exists",
				"An attempt was made to create a user that already exists");

		private String statusCode;
		private String errorMessage;
		private String developerMessage;

		private DuplicateFound(String statusCode, String errorMessage,
				String developerMessage) {
			this.statusCode = statusCode;
			this.errorMessage = errorMessage;
			this.developerMessage = developerMessage;
		}

		public String getStatusCode() {
			return statusCode;
		}

		public String getErrorMessage() {
			return errorMessage;
		}

		public String getDeveloperMessage() {
			return developerMessage;
		}

	}
}
