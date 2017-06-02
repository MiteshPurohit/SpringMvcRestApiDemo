package com.cloudzon.config;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import com.cloudzon.repository.UserRepository;
import com.cloudzon.security.AccessDeniedHandlerImpl;
import com.cloudzon.security.AuthenticationTokenProcessingFilter;
import com.cloudzon.security.CustomAuthenticationProvider;
import com.cloudzon.security.CustomeUserDetailService;
import com.cloudzon.security.InMemoryTokenStore;
import com.cloudzon.security.UnauthorizedEntryPoint;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Resource
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncode;

	@Autowired
	private InMemoryTokenStore tokenStore;


	@Bean
	public CustomAuthenticationProvider authenticationProvider() {
		return new CustomAuthenticationProvider(userDetailsService(),
				bCryptPasswordEncode);
	}

	@Bean
	public AuthenticationTokenProcessingFilter authenticationTokenProcessingFilter() {
		// TODO change
		
		/* InMemoryTokenStore */
		return new AuthenticationTokenProcessingFilter(userDetailsService(),
				tokenStore, "ANONYMOUS", "anonymousUser");
		
		/* JdbcTokenStore */
		/*
		 * return new AuthenticationTokenProcessingFilter(userDetailsService(),
		 * jdbcTokenStore, "ANONYMOUS", "anonymousUser");
		 */
	}

	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return new AccessDeniedHandlerImpl();
	}

	@Bean
	public UnauthorizedEntryPoint unauthorizedEntryPoint() {
		return new UnauthorizedEntryPoint();
	}

	@Override
	protected UserDetailsService userDetailsService() {
		return new CustomeUserDetailService(userRepository);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder managerBuilder)
			throws Exception {
		// TODO change
		managerBuilder.authenticationProvider(authenticationProvider());
		// managerBuilder.userDetailsService(userDetailsService());
	}

	@Override
	public void configure(WebSecurity webSecurity) {
		webSecurity.ignoring().antMatchers("/resources/**");
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
				.csrf()
				.disable()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.addFilterBefore(authenticationTokenProcessingFilter(),
						FilterSecurityInterceptor.class).exceptionHandling()
				.accessDeniedHandler(accessDeniedHandler())
				.authenticationEntryPoint(unauthorizedEntryPoint());
	}
}
