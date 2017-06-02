package com.cloudzon.security;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import com.cloudzon.common.Constant;
import com.cloudzon.model.User;

public class AuthenticationTokenProcessingFilter extends GenericFilterBean {

	private final UserDetailsService userDetailsService;

	private final InMemoryTokenStore tokenStore;

	private final Object principal;
	private final List<GrantedAuthority> authorities;

	public AuthenticationTokenProcessingFilter(
			UserDetailsService userDetailsService,
			InMemoryTokenStore tokenStore, String authority, String principal) {
		this.userDetailsService = userDetailsService;
		this.tokenStore = tokenStore;
		this.principal = principal;
		this.authorities = AuthorityUtils.createAuthorityList(authority);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = null;
		String authToken = null;
		User objUser = null;
		try {
			if (!(request instanceof HttpServletRequest)) {
				throw new RuntimeException("Expecting a HTTP request");
			}

			httpRequest = (HttpServletRequest) request;

			if (StringUtils.hasText(httpRequest.getHeader("Authorization"))) {
				authToken = httpRequest.getHeader("Authorization");
			} else if (StringUtils.hasText(httpRequest
					.getParameter("access_token"))) {
				authToken = httpRequest.getParameter("access_token");
				authToken = URLDecoder.decode(authToken, Constant.UTF8);
			}

			if (StringUtils.hasText(authToken)) {
				// TODO change
				objUser = this.tokenStore.readAccessToken(authToken);
				setAuthentication(objUser, httpRequest);
			} else {
				HttpSession session = httpRequest.getSession(false);
				if (session != null) {
					objUser = (User) session
							.getAttribute(Constant.SESSION_USER);
				}
			}
			chain.doFilter(request, response);
		} finally {

		}

	}

	private void setAuthentication(User objUser, HttpServletRequest request) {
		UserDetails userDetails = null;
		UsernamePasswordAuthenticationToken authentication = null;
		if (null != objUser) {
			userDetails = this.userDetailsService.loadUserByUsername(objUser
					.getEmail());
			if (null != userDetails) {
				authentication = new UsernamePasswordAuthenticationToken(
						objUser.getEmail(), objUser.getPassword(),
						userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource()
						.buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(
						authentication);
			} else {
				authentication = new UsernamePasswordAuthenticationToken(
						this.principal, null, this.authorities);
				SecurityContextHolder.getContext().setAuthentication(
						authentication);
			}
		} else {
			authentication = new UsernamePasswordAuthenticationToken(
					this.principal, null, this.authorities);
			SecurityContextHolder.getContext()
					.setAuthentication(authentication);
		}
	}
}
