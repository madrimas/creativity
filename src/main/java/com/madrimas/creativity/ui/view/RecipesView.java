package com.madrimas.creativity.ui.view;

import com.madrimas.creativity.model.Recipe;
import com.madrimas.creativity.service.RecipeService;
import com.madrimas.creativity.ui.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.Comparator;

@Route(value="", layout = MainLayout.class)
@PageTitle("Recipes | Creativity")
public class RecipesView extends VerticalLayout {

	public static final String ROUTE = "";

	Grid<Recipe> grid = new Grid<>(Recipe.class);
	TextField filterText = new TextField();

	private final RecipeService recipeService;

	public RecipesView(RecipeService recipeService) {
		this.recipeService = recipeService;

		addClassName("recipes-view");
		setSizeFull();
		configureGrid();

		add(getToolBar(), grid);
		updateList();
	}

	private void configureGrid() {
		grid.addClassName("recipes-grid");
		grid.setSizeFull();
		grid.setColumns("title", "minutes", "instruction");
		grid.addColumn(recipe -> {
			Integer difficulty = recipe.getDifficulty();
			return difficulty == null ? "-" : getDifficultyNameByLevel(difficulty);
		}).setHeader("Difficulty").setComparator(Comparator.comparingInt(Recipe::getDifficulty));

		grid.getColumns().forEach(column -> column.setAutoWidth(true));

		grid.asSingleSelect().addValueChangeListener(evt -> editRecipe(evt.getValue()));
	}

	private void editRecipe(Recipe recipe) {
		if(recipe != null){
			UI.getCurrent().navigate(RecipeCreationView.class, recipe.getId());
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

		HorizontalLayout toolbar = new HorizontalLayout(filterText, addRecipeButton);
		toolbar.addClassName("toolbar");
		return toolbar;
	}

	private void addRecipe() {
		UI.getCurrent().navigate(RecipeCreationView.ROUTE);
	}

	private void updateList() {
		grid.setItems(recipeService.getRecipesForCurrentUser(filterText.getValue()));
	}

}
