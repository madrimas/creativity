package com.madrimas.creativity.dao;

import com.madrimas.creativity.model.Ingredient;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IngredientRepository extends CrudRepository<Ingredient, Integer> {

	Ingredient findById(int id);

	Ingredient findByName(String title);

	List<Ingredient> findAll();

	@Query("select i from Ingredient i where lower(i.name) like lower(concat('%', :filter, '%'))")
	List<Ingredient> search(@Param("filter") String filter);

}

