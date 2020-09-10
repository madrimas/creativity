package com.madrimas.creativity.ui.view;

import com.madrimas.creativity.controller.IngredientController;
import com.madrimas.creativity.model.Ingredient;
import com.madrimas.creativity.service.IngredientService;
import com.madrimas.creativity.service.UserService;
import com.madrimas.creativity.ui.IngredientForm;
import com.madrimas.creativity.ui.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import static com.vaadin.flow.component.notification.Notification.Position.TOP_CENTER;

@Route(value = IngredientsView.ROUTE, layout = MainLayout.class)
@PageTitle("Ingredients | Cookbook")
public class IngredientsView extends VerticalLayout {

	public static final String ROUTE = "ingredient";

	private final IngredientForm form;
	Grid<Ingredient> grid = new Grid<>(Ingredient.class);
	TextField filterText = new TextField();

	private final IngredientController ingredientController;

	private final IngredientService ingredientService;

	private final UserService userService;

	public IngredientsView(IngredientController ingredientController, IngredientService ingredientService, UserService userService) {
		this.ingredientController = ingredientController;
		this.ingredientService = ingredientService;
		this.userService = userService;

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
			Integer currentUserId = userService.getCurrentUser().getId();
			Integer ingredientAuthorId = ingredient.getAuthorId();
			if (ingredientAuthorId == null || currentUserId.equals(ingredientAuthorId)) {
				form.setIngredient(ingredient);
				form.setVisible(true);
				addClassName("editing");
			} else {
				closeEditor();
				showWarningMessage();
			}
		}
	}

	private void showWarningMessage() {
		Span warningMessage = new Span("You can modify only yours ingredients");
		warningMessage.getStyle().set("color", "var(--lumo-error-color");
		Notification successNotification = new Notification(warningMessage);
		successNotification.setDuration(5000);
		successNotification.setPosition(TOP_CENTER);
		successNotification.open();
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
