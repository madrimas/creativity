package com.madrimas.creativity.service;

import com.madrimas.creativity.dao.RecipeDAO;
import com.madrimas.creativity.dao.RecipeRepository;
import com.madrimas.creativity.model.Ingredient;
import com.madrimas.creativity.model.Recipe;
import com.madrimas.creativity.pojo.FindRecipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RecipeService {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	RecipeRepository recipeRepository;

	@Autowired
	UserService userService;

	@Autowired
	RecipeDAO recipeDAO;

	public List<Recipe> getRecipesForCurrentUser(String filter) {
		Integer currentUserId = userService.getCurrentUser().getId();
		if (StringUtils.isEmpty(filter)) {
			return recipeRepository.findAllForCurrentUser(currentUserId);
		} else {
			return recipeRepository.searchForCurrentUser(currentUserId, filter);
		}
	}

	@Transactional
	public Recipe getRecipeFullyInitialized(Integer recipeId) {
		Recipe recipe = entityManager.find(Recipe.class, recipeId);
		recipe.getIngredients().size();
		return recipe;
	}

	public List<Recipe> findRecipes(FindRecipe findRecipe) {
		Integer currentUserId = userService.getCurrentUser().getId();
		List<Recipe> recipes = recipeDAO.findRecipes(currentUserId, findRecipe);

		Set<Ingredient> ingredientsFilter = findRecipe.getIngredients();
		if (!ingredientsFilter.isEmpty()) {
			recipes = recipes.stream()
					.filter((recipe -> recipe.getIngredients().containsAll(ingredientsFilter)))
					.collect(Collectors.toList());
		}

		return recipes;
	}
}
