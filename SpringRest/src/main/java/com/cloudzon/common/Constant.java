package com.cloudzon.common;

public interface Constant {

	String SESSION_USER = "sessionUser";

	String DATE_FORMAT = "dd/MM/yy";
	String DATE_FORMAT_WITH_TIME = "dd/MM/yyyy HH:mm:ss.SSS";

	// template name for email
	String TEMPLATE_EMAIL_CREATE_ACCOUNT = "createAccount.ftl";
	String TEMPLATE_EMAIL_CHANGE_PASSWORD = "changePassword.ftl";
	String TEMPLATE_EMAIL_RESET_PASSWORD = "resetPassword.ftl";

	// pattern
	String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	// common
	String UTF8 = "UTF-8";

	// response key
	String MESSAGE_KEY = "message";
	String ERROR_KEY = "error";

	int ERROR_LOGIN_CODE = 1;
	int ERROR_ROOM_TIME_OUT = 5;
	int ERROR_PAYMENT_CODE = 2;
	String ERROR_LOGIN_MESSAGE = "Please Login...";

}
