package com.madrimas.creativity.ui.view;

import com.madrimas.creativity.model.Recipe;
import com.madrimas.creativity.service.RecipeService;
import com.madrimas.creativity.ui.MainLayout;
import com.madrimas.creativity.ui.RecipeForm;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value = RecipeDetailsView.ROUTE, layout = MainLayout.class)
@PageTitle("Recipe details | Creativity")
public class RecipeDetailsView extends HorizontalLayout implements HasUrlParameter<Integer> {

	public static final String ROUTE = "recipeDetails";

	private RecipeForm form;

	private final RecipeService recipeService;

	public RecipeDetailsView(RecipeService recipeService) {
		this.recipeService = recipeService;
	}

	@Override
	public void setParameter(BeforeEvent event, Integer recipeId) {
		Recipe recipe = recipeService.getRecipeFullyInitialized(recipeId);

		form = new RecipeForm(List.copyOf(recipe.getIngredients()), false);
		form.addListener(RecipeForm.CloseEvent.class, e -> closeEditor());

		add(form);
		form.setRecipe(recipe);
	}

	private <T extends ComponentEvent<?>> void closeEditor() {
		UI.getCurrent().navigate(RecipesView.ROUTE);
	}
}
