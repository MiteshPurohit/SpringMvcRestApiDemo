package com.cloudzon.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * {@link AuthenticationEntryPoint} that rejects all requests with an
 * unauthorized error message.
 * 
 * @author Philip W. Sorst <philip@sorst.net>
 */
public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException authException)
			throws IOException, ServletException {

		/*
		 * response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
		 * "Unauthorized: Authentication token was either missing or invalid.");
		 */
		// throw new AuthorizationException(authException.getMessage());

		if (!response.isCommitted()) {
			if (request.getContentType() != null
					&& request.getContentType().equalsIgnoreCase(
							"application/json")) {
				response.setContentType("application/json");
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				// response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				PrintWriter out = response.getWriter();
				out.println("{");
				out.println("\"status\": \"FAIL\",");
				out.println("\"message\": \"Unauthorized: Authentication token was either missing or invalid.\"");
				out.println("}");
				out.close();
			} else {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			}
		}
	}
}