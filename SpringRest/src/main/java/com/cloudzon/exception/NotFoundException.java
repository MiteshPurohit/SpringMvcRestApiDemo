package com.cloudzon.exception;

public class NotFoundException extends BaseWebApplicationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4553101964302123808L;

	public NotFoundException(NotFound notFound) {
		super(404, notFound.getStatusCode(), notFound.getErrorMessage(),
				notFound.getDeveloperMessage());
	}

	public enum NotFound {
		UserNotFound("40401", "User Not Found",
				"No User could be found for that Id"), DefaultRoleNotSet(
				"40402", "Default role not set by admin",
				"Admin not set role in database"), TokenNotFound("40403",
				"Token Not Found", "No token could be found for that Id"),ImageNotFound("40404",
						"Image Not Found", "Plese select Image File");

		private String statusCode;
		private String errorMessage;
		private String developerMessage;

		private NotFound(String statusCode, String errorMessage,
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
