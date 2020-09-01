package com.madrimas.creativity.service;

import com.madrimas.creativity.HibernateService;
import com.madrimas.creativity.dao.RecipeRepository;
import com.madrimas.creativity.model.Recipe;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class RecipeService {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	RecipeRepository recipeRepository;

	@Autowired
	UserService userService;

	@Autowired
	HibernateService hibernateService;

	public List<Recipe> getRecipesForCurrentUser(String filter) {
		Integer currentUserId = userService.getCurrentUser().getId();
		if(StringUtils.isEmpty(filter)){
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
}
