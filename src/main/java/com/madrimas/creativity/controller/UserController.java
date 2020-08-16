package com.madrimas.creativity.controller;

import com.madrimas.creativity.dao.UserRepository;
import com.madrimas.creativity.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/user")
public class UserController {

	@Autowired
	UserRepository userRepository;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public User getUser(@PathVariable int id) {
		return userRepository.findById(id);
	}

	@RequestMapping(value = "/{login}", method = RequestMethod.GET)
	public User getUserByLogin(@PathVariable String login) {
		return userRepository.findByLogin(login);
	}

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public Iterable<User> getUsers() {
		return userRepository.findAll();
	}
}
