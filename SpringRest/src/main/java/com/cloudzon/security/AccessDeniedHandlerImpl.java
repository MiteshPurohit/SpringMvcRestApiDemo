package com.cloudzon.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request,
			HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException,
			ServletException {
		// throw new AuthorizationException(accessDeniedException.getMessage());
		if (!response.isCommitted()) {
			if (request.getContentType() != null
					&& request.getContentType().equalsIgnoreCase(
							"application/json")) {
				response.setContentType("application/json");
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				// response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				PrintWriter out = response.getWriter();
				out.println("{");
				out.println("\"status\":\"FAIL\",");
				out.println("\"message\":\""
						+ accessDeniedException.getMessage() + "\"");
				out.println("}");
				out.close();
			} else {
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
			}
		}
	}

}
