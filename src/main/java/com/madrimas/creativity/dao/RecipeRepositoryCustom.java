package com.madrimas.creativity.dao;

import com.madrimas.creativity.model.Recipe;
import com.madrimas.creativity.pojo.FindRecipe;

import java.util.List;

public interface RecipeRepositoryCustom {

	List<Recipe> findRecipes(Integer currentUserId, FindRecipe findRecipe);

}
