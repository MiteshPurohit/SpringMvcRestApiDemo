package com.cloudzon.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.cloudzon.repository.UserRepository;

public class CustomeUserDetailService implements UserDetailsService {

	private UserRepository userRepository;

	public CustomeUserDetailService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email)
			throws UsernameNotFoundException {

		com.cloudzon.model.User objUser = null;
		Collection<GrantedAuthority> objAuthorities = null;
		SimpleGrantedAuthority objAuthority = null;
		UserDetails userDetails = null;
		try {
			// get user data from database
			objUser = this.userRepository.getUserByEmail(email);
			if (null != objUser) {
				objAuthorities = new ArrayList<GrantedAuthority>();
				objAuthority = new SimpleGrantedAuthority("ROLE_USER");
				objAuthorities.add(objAuthority);
				userDetails = new User(email, objUser.getPassword(), true,
						true, true, true, objAuthorities);
			} else {
				throw new UsernameNotFoundException("Not Valid User");
			}
		} finally {
			objUser = null;
			// userRoles = null;
			objAuthorities = null;
			objAuthority = null;
		}
		return userDetails;
	}

}