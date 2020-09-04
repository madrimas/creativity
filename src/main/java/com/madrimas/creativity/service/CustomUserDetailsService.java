package com.madrimas.creativity.service;

import com.madrimas.creativity.dao.UserRepository;
import com.madrimas.creativity.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	public UserDetails loadUserByUsername(String login)
			throws UsernameNotFoundException {

		User user = userRepository.findByLogin(login);
		if (user == null) {
			throw new UsernameNotFoundException(
					"No user found with login: " + login);
		}

		return new org.springframework.security.core.userdetails.User
				(user.getLogin(),
						user.getPassword(),
						getAuthorities(Collections.emptyList()));
	}

	private static List<GrantedAuthority> getAuthorities(List<String> roles) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		for (String role : roles) {
			authorities.add(new SimpleGrantedAuthority(role));
		}
		return authorities;
	}
}