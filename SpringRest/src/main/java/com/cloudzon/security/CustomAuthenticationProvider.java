package com.cloudzon.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.cloudzon.exception.NotFoundException;
import com.cloudzon.exception.NotFoundException.NotFound;

public class CustomAuthenticationProvider implements AuthenticationProvider {

	private UserDetailsService userDetailsService;
	private PasswordEncoder passwordEncoder;

	public CustomAuthenticationProvider(UserDetailsService userDetailsService,
			PasswordEncoder passwordEncoder) {
		this.userDetailsService = userDetailsService;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		String username = authentication.getName();

		UserDetails user = userDetailsService.loadUserByUsername(username);

		if (user == null) {
			throw new NotFoundException(NotFound.UserNotFound);
		}

		String password = (String) authentication.getCredentials();
		if (this.passwordEncoder.matches(password, user.getPassword())) {
			return new UsernamePasswordAuthenticationToken(username,
					user.getPassword(), user.getAuthorities());
		} else {
			throw new BadCredentialsException("Wrong password.");
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
