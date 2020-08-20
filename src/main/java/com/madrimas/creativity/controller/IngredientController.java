package com.madrimas.creativity.controller;

import com.madrimas.creativity.dao.IngredientRepository;
import com.madrimas.creativity.model.Ingredient;
import com.madrimas.creativity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping(path = "/ingredient")
public class IngredientController {

	@Autowired
	IngredientRepository ingredientRepository;

	@Autowired
	UserService userService;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Ingredient getIngredient(@PathVariable int id) {
		return ingredientRepository.findById(id);
	}

	@RequestMapping(value = "/{title}", method = RequestMethod.GET)
	public Ingredient getIngredientByName(@PathVariable String title) {
		return ingredientRepository.findByName(title);
	}

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public Iterable<Ingredient> getIngredients() {
		return ingredientRepository.findAll();
	}

	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public Ingredient updateIngredient(Ingredient recipe) {
		recipe.setModificationDate(LocalDateTime.now());
		return ingredientRepository.save(recipe);
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Ingredient addIngredient(Ingredient recipe) {
		recipe.setAuthor(userService.getCurrentUser());
		recipe.setCreationDate(LocalDateTime.now());
		return ingredientRepository.save(recipe);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void deleteIngredient(@PathVariable int id) {
		ingredientRepository.deleteById(id);
	}

}