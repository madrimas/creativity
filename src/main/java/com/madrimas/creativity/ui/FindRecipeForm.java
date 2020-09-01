package com.madrimas.creativity.ui;

import com.madrimas.creativity.model.Ingredient;
import com.madrimas.creativity.model.Recipe;
import com.madrimas.creativity.pojo.FindRecipe;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import org.vaadin.gatanaso.MultiselectComboBox;

import java.util.List;

public class FindRecipeForm extends FormLayout {

	TextField title = new TextField("Title");
	ComboBox<FindRecipe.Compare> compare = new ComboBox<>("Compare");
	ComboBox<Recipe.Difficulty> difficulty = new ComboBox<>("Difficulty");
	IntegerField minutes = new IntegerField("Less than (minutes)");
	Checkbox privateOnly = new Checkbox("Private recipe");
	MultiselectComboBox<Ingredient> ingredients = new MultiselectComboBox<>("Ingredients");

	Button find = new Button("Find");
	Button cancel = new Button("Cancel");

	Binder<FindRecipe> binder = new BeanValidationBinder<>(FindRecipe.class);
	private FindRecipe recipe;

	public FindRecipeForm(List<Ingredient> availableIngredients) {
		addClassName("recipes-find-form");

		binder.forField(difficulty)
				.withConverter(new DifficultyToLevelConverter())
				.bind(FindRecipe::getDifficulty, FindRecipe::setDifficulty);
		binder.forField(compare)
				.bind(FindRecipe::getDifficultyCompare, FindRecipe::setDifficultyCompare);
		binder.bindInstanceFields(this);
		difficulty.setItems(Recipe.Difficulty.values());
		compare.setItems(FindRecipe.Compare.values());
		ingredients.setItems(availableIngredients);
		ingredients.setItemLabelGenerator(Ingredient::getName);

		add(title, compare, difficulty, minutes, ingredients, privateOnly, createButtonsLayout());
	}

	private Component createButtonsLayout() {
		find.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

		find.addClickShortcut(Key.ENTER, KeyModifier.CONTROL);
		cancel.addClickShortcut(Key.ESCAPE);

		find.addClickListener(click -> validateAndFind());
		cancel.addClickListener(click -> fireEvent(new FindRecipeForm.CloseEvent(this)));

		binder.addStatusChangeListener(evt -> find.setEnabled(binder.isValid()));

		return new HorizontalLayout(find, cancel);
	}

	public void setFindRecipe(FindRecipe recipe) {
		this.recipe = recipe;
	}

	private void validateAndFind() {
		try {
			binder.writeBean(recipe);
			fireEvent(new FindEvent(this, recipe));
		} catch (ValidationException e) {
			e.printStackTrace();
		}
	}

	// Events
	public static abstract class FindRecipeFormEvent extends ComponentEvent<FindRecipeForm> {
		private final FindRecipe recipe;

		protected FindRecipeFormEvent(FindRecipeForm source, FindRecipe recipe) {
			super(source, false);
			this.recipe = recipe;
		}

		public FindRecipe getRecipe() {
			return recipe;
		}
	}

	public static class FindEvent extends FindRecipeForm.FindRecipeFormEvent {
		FindEvent(FindRecipeForm source, FindRecipe recipe) {
			super(source, recipe);
		}
	}

	public static class CloseEvent extends FindRecipeForm.FindRecipeFormEvent {
		CloseEvent(FindRecipeForm source) {
			super(source, null);
		}
	}

	public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
	                                                              ComponentEventListener<T> listener) {
		return getEventBus().addListener(eventType, listener);
	}
}
