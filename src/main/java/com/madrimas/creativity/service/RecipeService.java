package com.madrimas.creativity.service;

import com.madrimas.creativity.HibernateService;
import com.madrimas.creativity.dao.RecipeRepository;
import com.madrimas.creativity.model.Recipe;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

@Service
public class RecipeService {

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

	public Recipe getRecipeFullyInitialized(Integer recipeId){
		EntityManager entityManager = hibernateService.getHibernateFactory().unwrap(SessionFactory.class).createEntityManager();
		return entityManager.find(Recipe.class, recipeId);
	}
}
