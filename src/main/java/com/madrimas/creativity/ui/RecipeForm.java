package com.madrimas.creativity.ui;

import com.madrimas.creativity.model.Ingredient;
import com.madrimas.creativity.model.Recipe;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import org.vaadin.gatanaso.MultiselectComboBox;

import java.util.List;

public class RecipeForm extends FormLayout {

	TextField title = new TextField("Title");
	ComboBox<Recipe.Difficulty> difficulty = new ComboBox<>("Difficulty"); //Vaadin issue #8858;
	IntegerField minutes = new IntegerField("Time");
	TextArea instruction = new TextArea("Instruction");
	Checkbox privateOnly = new Checkbox("Private recipe");
	MultiselectComboBox<Ingredient> ingredients = new MultiselectComboBox<>("Ingredients");

	com.vaadin.flow.component.button.Button save = new com.vaadin.flow.component.button.Button("Save");
	com.vaadin.flow.component.button.Button delete = new com.vaadin.flow.component.button.Button("Delete");
	com.vaadin.flow.component.button.Button close = new Button("Cancel");

	Binder<Recipe> binder = new BeanValidationBinder<>(Recipe.class);
	private Recipe recipe;

	public RecipeForm(List<Ingredient> availableIngredients) {
		addClassName("recipe-form");

		binder.readBean(recipe);

		binder.forField(difficulty)
				.withConverter(new DifficultyToLevelConverter())
				.bind(Recipe::getDifficulty, Recipe::setDifficulty);
		binder.bindInstanceFields(this);
		difficulty.setItems(Recipe.Difficulty.values());
		ingredients.setItems(availableIngredients);
		ingredients.setItemLabelGenerator(Ingredient::getName);

		add(title, difficulty, minutes, instruction, privateOnly, this.ingredients, createButtonsLayout());
	}

	private Component createButtonsLayout() {
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
		close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

		save.addClickShortcut(Key.ENTER, KeyModifier.CONTROL);
		close.addClickShortcut(Key.ESCAPE);

		save.addClickListener(click -> validateAndSave());
		delete.addClickListener(click -> fireEvent(new DeleteEvent(this, recipe)));
		close.addClickListener(click -> fireEvent(new CloseEvent(this)));

		binder.addStatusChangeListener(evt -> save.setEnabled(binder.isValid()));

		return new HorizontalLayout(save, delete, close);
	}

	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
		if(recipe.getId() != null){
			binder.readBean(this.recipe);
		}
	}

	private void validateAndSave() {

		try {
			binder.writeBean(recipe);
			fireEvent(new SaveEvent(this, recipe));
		} catch (ValidationException e) {
			e.printStackTrace();
		}
	}

	// Events
	public static abstract class RecipeFormEvent extends ComponentEvent<RecipeForm> {
		private final Recipe recipe;

		protected RecipeFormEvent(RecipeForm source, Recipe recipe) {
			super(source, false);
			this.recipe = recipe;
		}

		public Recipe getRecipe() {
			return recipe;
		}
	}

	public static class SaveEvent extends RecipeFormEvent {
		SaveEvent(RecipeForm source, Recipe recipe) {
			super(source, recipe);
		}
	}

	public static class DeleteEvent extends RecipeFormEvent {
		DeleteEvent(RecipeForm source, Recipe recipe) {
			super(source, recipe);
		}

	}

	public static class CloseEvent extends RecipeFormEvent {
		CloseEvent(RecipeForm source) {
			super(source, null);
		}
	}

	public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
	                                                              ComponentEventListener<T> listener) {
		return getEventBus().addListener(eventType, listener);
	}

}
