package com.madrimas.creativity.ui.view;

import com.madrimas.creativity.controller.IngredientController;
import com.madrimas.creativity.model.Ingredient;
import com.madrimas.creativity.service.IngredientService;
import com.madrimas.creativity.ui.IngredientForm;
import com.madrimas.creativity.ui.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = IngredientsView.ROUTE, layout = MainLayout.class)
@PageTitle("Ingredients | Creativity")
public class IngredientsView extends VerticalLayout {

	public static final String ROUTE = "ingredient";

	private final IngredientForm form;
	Grid<Ingredient> grid = new Grid<>(Ingredient.class);
	TextField filterText = new TextField();

	private IngredientController ingredientController;

	private IngredientService ingredientService;

	public IngredientsView(IngredientController ingredientController, IngredientService ingredientService) {
		this.ingredientController = ingredientController;
		this.ingredientService = ingredientService;

		addClassName("ingredients-view");
		setSizeFull();
		configureGrid();

		form = new IngredientForm();
		form.addListener(IngredientForm.SaveEvent.class, this::saveIngredient);
		form.addListener(IngredientForm.DeleteEvent.class, this::deleteIngredient);
		form.addListener(IngredientForm.CloseEvent.class, e -> closeEditor());

		Div content = new Div(grid, form);
		content.addClassName("content");
		content.setSizeFull();

		add(getToolBar(), content);
		updateList();
		closeEditor();
	}

	private void deleteIngredient(IngredientForm.DeleteEvent evt) {
		ingredientController.deleteIngredient(evt.getIngredient().getId());
		updateList();
		closeEditor();
	}

	private void saveIngredient(IngredientForm.SaveEvent evt) {
		ingredientController.addIngredient(evt.getIngredient());
		updateList();
		closeEditor();
	}

	private HorizontalLayout getToolBar() {
		filterText.setPlaceholder("Filter by name...");
		filterText.setClearButtonVisible(true);
		filterText.setValueChangeMode(ValueChangeMode.LAZY);
		filterText.addValueChangeListener(e -> updateList());

		Button addContactButton = new Button("Add ingredient", click -> addIngredient());

		HorizontalLayout toolbar = new HorizontalLayout(filterText, addContactButton);
		toolbar.addClassName("toolbar");
		return toolbar;
	}

	private void addIngredient() {
		grid.asSingleSelect().clear();
		editIngredient(new Ingredient());
	}

	private void configureGrid() {
		grid.addClassName("ingredient-grid");
		grid.setSizeFull();
		grid.setColumns("name", "description");

		grid.getColumns().forEach(col -> col.setAutoWidth(true));

		grid.asSingleSelect().addValueChangeListener(evt -> editIngredient(evt.getValue()));
	}

	private void editIngredient(Ingredient ingredient) {
		if (ingredient == null) {
			closeEditor();
		} else {
			form.setIngredient(ingredient);
			form.setVisible(true);
			addClassName("editing");
		}
	}

	private void closeEditor() {
		form.setIngredient(null);
		form.setVisible(false);
		removeClassName("editing");
	}

	private void updateList() {
		grid.setItems(ingredientService.getIngredients(filterText.getValue()));
	}
}
