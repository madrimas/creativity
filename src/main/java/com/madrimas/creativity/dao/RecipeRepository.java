package com.madrimas.creativity.dao;

import com.madrimas.creativity.model.Recipe;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RecipeRepository extends CrudRepository<Recipe, Integer> {

	Recipe findById(int id);

	Recipe findByTitle(String title);

	@Query("select r from Recipe r where r.authorId = :currentUserId or r.privateOnly = false")
	List<Recipe> findAllForCurrentUser(Integer currentUserId);

	@Query("select r " +
			"from Recipe r " +
			"where (r.authorId = :currentUserId or r.privateOnly = false)" +
				"and (lower(r.title) like lower(concat('%', :filter, '%')))")
	List<Recipe> searchForCurrentUser(Integer currentUserId, String filter);
}
