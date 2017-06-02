package com.cloudzon.service.impl;

import java.security.Principal;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.cloudzon.dto.AccessTokenContainer;
import com.cloudzon.dto.UserDto;
import com.cloudzon.dto.UserLoginDto;
import com.cloudzon.exception.AuthenticationException;
import com.cloudzon.exception.DuplicateException;
import com.cloudzon.exception.DuplicateException.DuplicateFound;
import com.cloudzon.exception.NotFoundException;
import com.cloudzon.exception.NotFoundException.NotFound;
import com.cloudzon.model.User;
import com.cloudzon.repository.UserRepository;
import com.cloudzon.security.InMemoryTokenStore;
import com.cloudzon.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory
			.getLogger(UserServiceImpl.class);

	@Resource
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncode;

	@Autowired
	private InMemoryTokenStore tokenStore;

	@Override
	@Transactional
	public User getUserData(Principal principal) {
		logger.info("getUserData");
		User objUser = null;
		if (principal != null && principal.getName() != null)
			objUser = this.userRepository.getUserByEmail(principal.getName());
		return objUser;
	}

	@Override
	public AccessTokenContainer getAccessTokenContainer(User user) {
		logger.info("getAccessTokenContainer");
		return this.tokenStore.generateAccessToken(user);

	}

	@Override
	@Transactional(rollbackFor = { Exception.class }, isolation = Isolation.READ_COMMITTED)
	public User login(UserLoginDto loginDto) {
		logger.info("login Start");
		User objUser = this.userRepository.getUserByEmail(loginDto.getEmail());
		if (objUser != null) {
			// if (objUser.getIsVerified()) {
			if (this.bCryptPasswordEncode.matches(loginDto.getPassword(),
					objUser.getPassword())) {
				return objUser;
			} else {
				throw new AuthenticationException();
			}
			/*
			 * } else { throw new AuthenticationException(
			 * "Please confirm your account", "User not verified"); }
			 */
		} else {
			throw new NotFoundException(NotFound.UserNotFound);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserDto> userList(User user) {
		logger.info("userList all start");
		try {
			return this.userRepository.getUsers(user.getEmail());
		} finally {
			logger.info("userList all end");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public UserDto userList(Long userId) {
		logger.info("userList one start");
		try {
			return this.userRepository.getUserById(userId);
		} finally {
			logger.info("userList one end");
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
	public void signUp(UserDto userDto) {
		User user = null;
		if (userDto.getId() != null) {
			user = this.userRepository.getOne(userDto.getId());
			user.setFirstName(userDto.getFirstName());
			user.setLastName(userDto.getLastName());
			this.userRepository.save(user);
		} else {
			user = this.userRepository.getUserByEmail(userDto.getEmail());
			if (user != null) {
				throw new DuplicateException(DuplicateFound.DuplicateUserFound);
			}
			user = new User();
			user.setFirstName(userDto.getFirstName());
			user.setLastName(userDto.getLastName());
			user.setEmail(userDto.getEmail());
			user.setPassword(this.bCryptPasswordEncode.encode(userDto
					.getPassword()));
			this.userRepository.save(user);
		}

	}
}
