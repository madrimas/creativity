package com.madrimas.creativity.dao;

import com.madrimas.creativity.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {

	User findById(int id);

}
