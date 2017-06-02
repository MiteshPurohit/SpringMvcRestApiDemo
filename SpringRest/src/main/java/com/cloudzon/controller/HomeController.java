package com.cloudzon.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cloudzon.common.Constant;
import com.cloudzon.model.User;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

	private static final Logger logger = LoggerFactory
			.getLogger(HomeController.class);

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG,
				DateFormat.LONG, locale);

		String formattedDate = dateFormat.format(date);

		model.addAttribute("serverTime", formattedDate);

		return "home";
	}

	@RequestMapping(value = "sign-up", method = RequestMethod.GET)
	public String signup() {
		return "signUp";
	}

	@RequestMapping(value = "user-list", method = RequestMethod.GET)
	public String userList(HttpSession session) {
		User objUser = (User) session.getAttribute(Constant.SESSION_USER);
		if (objUser != null) {
			return "UserList";
		} else {
			return "home";
		}
	}

	@RequestMapping(value = "update-user/{userId}", method = RequestMethod.GET)
	public String updateUser(@PathVariable Long userId, HttpSession session) {
		User objUser = (User) session.getAttribute(Constant.SESSION_USER);
		if (objUser != null) {
			return "UpdateUser";
		} else {
			return "home";
		}
	}
}
