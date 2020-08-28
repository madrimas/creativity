package com.madrimas.creativity.controller;

import com.madrimas.creativity.dao.RecipeRepository;
import com.madrimas.creativity.model.Recipe;
import com.madrimas.creativity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping(path = "/recipe")
public class RecipeController {

	@Autowired
	RecipeRepository recipeRepository;

	@Autowired
	UserService userService;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Recipe getRecipe(@PathVariable int id) {
		return recipeRepository.findById(id);
	}

	@RequestMapping(value = "/{title}", method = RequestMethod.GET)
	public Recipe getRecipeByTitle(@PathVariable String title) {
		return recipeRepository.findByTitle(title);
	}

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public Iterable<Recipe> getRecipes() {
		return recipeRepository.findAll();
	}

	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public Recipe updateRecipe(Recipe recipe) {
		recipe.setModificationDate(LocalDateTime.now());
		return recipeRepository.save(recipe);
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Recipe addRecipe(Recipe recipe) {
		recipe.setAuthor(userService.getCurrentUser());
		recipe.setCreationDate(LocalDateTime.now());
		return recipeRepository.save(recipe);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void deleteRecipe(@PathVariable int id) {
		recipeRepository.deleteById(id);
	}
}