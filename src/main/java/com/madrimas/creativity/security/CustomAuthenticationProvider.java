package com.madrimas.creativity.security;

import com.madrimas.creativity.controller.UserController;
import com.madrimas.creativity.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	UserController userController;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String login = authentication.getName();
		String password = authentication.getCredentials().toString();

		User user = userController.getUserByLogin(login);
		if (user == null || !password.equals(user.getPassword())) {
			throw new BadCredentialsException("Authentication failed");
		}

		return new UsernamePasswordAuthenticationToken(login, password, Collections.emptyList());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
