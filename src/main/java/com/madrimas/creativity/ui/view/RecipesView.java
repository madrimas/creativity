package com.madrimas.creativity.ui.view;

import com.madrimas.creativity.model.Recipe;
import com.madrimas.creativity.service.RecipeService;
import com.madrimas.creativity.ui.MainLayout;
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
	}

	private String getDifficultyNameByLevel(int difficultyLevel) {
		return switch (difficultyLevel) {
			case 1 -> Recipe.DifficultyLevel.EASY;
			case 2 -> Recipe.DifficultyLevel.MEDIUM;
			case 3 -> Recipe.DifficultyLevel.HARD;
			default -> "Unknown level";
		};
	}

	private HorizontalLayout getToolBar() {
		filterText.setPlaceholder("Filter by title...");
		filterText.setClearButtonVisible(true);
		filterText.setValueChangeMode(ValueChangeMode.LAZY);
		filterText.addValueChangeListener(e -> updateList());

		HorizontalLayout toolbar = new HorizontalLayout(filterText);
		toolbar.addClassName("toolbar");
		return toolbar;
	}

	private void updateList() {
		grid.setItems(recipeService.getRecipesForCurrentUser(filterText.getValue()));
	}

}
