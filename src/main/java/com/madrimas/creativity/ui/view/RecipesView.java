package com.madrimas.creativity.ui.view;

import com.madrimas.creativity.controller.IngredientController;
import com.madrimas.creativity.model.Ingredient;
import com.madrimas.creativity.model.Recipe;
import com.madrimas.creativity.pojo.FindRecipe;
import com.madrimas.creativity.service.RecipeService;
import com.madrimas.creativity.service.UserService;
import com.madrimas.creativity.ui.FindRecipeForm;
import com.madrimas.creativity.ui.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.Comparator;
import java.util.List;

@Route(value="", layout = MainLayout.class)
@PageTitle("Recipes | Cookbook")
public class RecipesView extends VerticalLayout {

	public static final String ROUTE = "";

	Grid<Recipe> grid = new Grid<>(Recipe.class);
	TextField filterText = new TextField();

	private final FindRecipeForm form;

	private final RecipeService recipeService;

	private final UserService userService;

	public RecipesView(RecipeService recipeService, UserService userService, IngredientController ingredientController) {
		this.recipeService = recipeService;
		this.userService = userService;

		addClassName("recipes-view");
		setSizeFull();
		configureGrid();

		form = new FindRecipeForm(((List<Ingredient>) ingredientController.getIngredients()));
		form.addListener(FindRecipeForm.FindEvent.class, this::findRecipes);
		form.addListener(FindRecipeForm.CloseEvent.class, e -> closeSearchTab());
		form.setVisible(false);

		Div content = new Div(grid, form);
		content.addClassName("content");
		content.setSizeFull();

		add(getToolBar(), content);
		updateList();
	}

	private void findRecipes(FindRecipeForm.FindEvent event) {
		List<Recipe> filteredRecipes = recipeService.findRecipes(event.getRecipe());
		updateList(filteredRecipes);
	}


	private void closeSearchTab() {
		form.setFindRecipe(null);
		form.setVisible(false);
		removeClassName("editing");
	}

	private void configureGrid() {
		grid.addClassName("recipes-grid");
		grid.setSizeFull();
		grid.setColumns("title", "minutes");
		grid.addColumn(recipe -> {
			Integer difficulty = recipe.getDifficulty();
			return difficulty == null ? "-" : getDifficultyNameByLevel(difficulty);
		}).setHeader("Difficulty").setComparator(Comparator.comparingInt(Recipe::getDifficulty));

		grid.getColumns().forEach(column -> column.setAutoWidth(true));

		grid.asSingleSelect().addValueChangeListener(evt -> editRecipe(evt.getValue()));
	}

	private void editRecipe(Recipe recipe) {
		if (recipe != null) {
			Integer currentUserId = userService.getCurrentUser().getId();
			if (currentUserId.equals(recipe.getAuthorId())) {
				UI.getCurrent().navigate(RecipeCreationView.class, recipe.getId());
			} else {
				UI.getCurrent().navigate(RecipeDetailsView.class, recipe.getId());
			}
		}
	}

	private String getDifficultyNameByLevel(int difficultyLevel) {
		return switch (difficultyLevel) {
			case 1 -> Recipe.Difficulty.Easy.name();
			case 2 -> Recipe.Difficulty.Medium.name();
			case 3 -> Recipe.Difficulty.Hard.name();
			default -> "Unknown level";
		};
	}

	private HorizontalLayout getToolBar() {
		filterText.setPlaceholder("Filter by title...");
		filterText.setClearButtonVisible(true);
		filterText.setValueChangeMode(ValueChangeMode.LAZY);
		filterText.addValueChangeListener(e -> updateList());

		Button addRecipeButton = new Button("Add recipe", click -> addRecipe());
		Button findRecipesButton = new Button("Find recipes", click -> searchRecipes());

		HorizontalLayout toolbar = new HorizontalLayout(filterText, addRecipeButton, findRecipesButton);
		toolbar.addClassName("toolbar");
		return toolbar;
	}

	private void searchRecipes() {
		grid.asSingleSelect().clear();
		searchRecipeForm(new FindRecipe());
	}

	private void searchRecipeForm(FindRecipe findRecipe) {
		if (findRecipe == null) {
			closeSearchTab();
		} else {
			form.setFindRecipe(findRecipe);
			form.setVisible(true);
			addClassName("editing");
		}
	}

	private void addRecipe() {
		UI.getCurrent().navigate(RecipeCreationView.ROUTE);
	}

	private void updateList() {
		grid.setItems(recipeService.getRecipesForCurrentUser(filterText.getValue()));
	}

	private void updateList(List<Recipe> filteredRecipes) {
		grid.setItems(filteredRecipes);
	}

}
