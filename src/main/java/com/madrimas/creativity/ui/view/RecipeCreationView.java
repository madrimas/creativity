package com.madrimas.creativity.ui.view;

import com.madrimas.creativity.controller.IngredientController;
import com.madrimas.creativity.controller.RecipeController;
import com.madrimas.creativity.model.Ingredient;
import com.madrimas.creativity.model.Recipe;
import com.madrimas.creativity.service.RecipeService;
import com.madrimas.creativity.ui.MainLayout;
import com.madrimas.creativity.ui.RecipeForm;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.*;

import java.util.List;

@Route(value = RecipeCreationView.ROUTE, layout = MainLayout.class)
@PageTitle("Create recipe | Creativity")
public class RecipeCreationView extends HorizontalLayout implements HasUrlParameter<Integer> {

	public static final String ROUTE = "recipeCreation";

	private final RecipeForm form;

	private final RecipeController recipeController;

	private final RecipeService recipeService;

	private final IngredientController ingredientController;

	private Recipe recipe;

	public RecipeCreationView(RecipeController recipeController, IngredientController ingredientController, RecipeService recipeService) {
		this.recipeController = recipeController;
		this.ingredientController = ingredientController;
		this.recipeService = recipeService;

		form = new RecipeForm(((List<Ingredient>) ingredientController.getIngredients()));
		form.addListener(RecipeForm.SaveEvent.class, this::addRecipe);
		form.addListener(RecipeForm.DeleteEvent.class, this::deleteRecipe);
		form.addListener(RecipeForm.CloseEvent.class, e -> closeEditor());

		add(form);
	}

	private <T extends ComponentEvent<?>> void addRecipe(RecipeForm.SaveEvent event) {
		Recipe recipe = event.getRecipe();
		if (recipe.getId() == null) {
			recipeController.addRecipe(recipe);
		} else { // TODO check author
			recipeController.updateRecipe(recipe);
		}
		UI.getCurrent().navigate(RecipesView.ROUTE);
	}

	private <T extends ComponentEvent<?>> void deleteRecipe(RecipeForm.DeleteEvent event) {
		recipeController.deleteRecipe(event.getRecipe().getId());
		UI.getCurrent().navigate(RecipesView.ROUTE);
	}

	private <T extends ComponentEvent<?>> void closeEditor() {
		UI.getCurrent().navigate(RecipesView.ROUTE);
	}

	@Override
	public void setParameter(BeforeEvent event, @OptionalParameter Integer recipeId) {
		if(recipeId != null){
			recipe = recipeService.getRecipeFullyInitialized(recipeId);
		} else {
			recipe = new Recipe();
		}
		form.setRecipe(recipe);
	}
}
