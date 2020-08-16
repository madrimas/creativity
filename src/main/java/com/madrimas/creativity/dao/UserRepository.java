package com.madrimas.creativity.dao;

import com.madrimas.creativity.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<User, Integer> {

	User findById(int id);

	@Query("select u from User u where u.login = :login")
	User findByLogin(@Param("login") String login);

}
