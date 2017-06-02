package com.cloudzon.controller;

import java.security.Principal;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cloudzon.dto.AccessTokenContainer;
import com.cloudzon.dto.ResponseMessageDto;
import com.cloudzon.dto.UserDto;
import com.cloudzon.dto.UserLoginDto;
import com.cloudzon.exception.AuthorizationException;
import com.cloudzon.model.User;
import com.cloudzon.service.UserService;

@Controller
@RequestMapping("user")
public class UserController {

	private static final Logger logger = LoggerFactory
			.getLogger(UserController.class);

	@Resource(name = "userService")
	private UserService userService;

	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public AccessTokenContainer login(@RequestBody UserLoginDto loginDto) {
		logger.info("login");
		return this.userService.getAccessTokenContainer(this.userService
				.login(loginDto));
	}

	@RequestMapping(value = "signupOrUpdate", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public ResponseMessageDto signupUser(@RequestBody UserDto userDto,
			HttpServletRequest request) {
		logger.info("signupUser");
		this.userService.signUp(userDto);
		return new ResponseMessageDto("Signup done");
	}

	@RequestMapping(value = "/userList", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public List<UserDto> userList(Principal principal) {
		logger.info("userList");
		User objUser = this.userService.getUserData(principal);
		if (objUser != null) {
			return this.userService.userList(objUser);
		} else {
			throw new AuthorizationException("Session Expired");
		}
	}

	@RequestMapping(value = "/userList/{userId}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public UserDto userList(@PathVariable Long userId, Principal principal) {
		logger.info("userList");
		User objUser = this.userService.getUserData(principal);
		if (objUser != null) {
			return this.userService.userList(userId);
		} else {
			throw new AuthorizationException("Session Expired");
		}
	}
}
