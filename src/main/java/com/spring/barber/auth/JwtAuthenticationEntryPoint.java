package com.spring.barber.auth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException authenticationException) throws IOException {

			httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
			httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

			final String invalid = (String) httpServletRequest.getAttribute("invalid");
			final String expired = (String) httpServletRequest.getAttribute("expired");

			final Map<String, Object> body = new HashMap<>();
			body.put("statusCode", HttpServletResponse.SC_UNAUTHORIZED);

			if (authenticationException instanceof BadCredentialsException) {
				body.put("message", "Bad credentials");
			}
			else if (authenticationException instanceof DisabledException) {
				body.put("message", "Your account has been locked");
			}
			else if (invalid != null) {
				body.put("message", "JWT token is invalid");
			}
			else if (expired != null) {
				body.put("message", "JWT token expired");
			}
			else {
				body.put("message", "Unauthorized");
			}

			final ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(httpServletResponse.getOutputStream(), body);

    }

}