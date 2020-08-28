package com.madrimas.creativity.ui;

import com.madrimas.creativity.model.Ingredient;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

public class IngredientForm extends FormLayout {

	TextField name = new TextField("Name");
	TextArea description = new TextArea("Description");

	Button save = new Button("Save");
	Button delete = new Button("Delete");
	Button close = new Button("Cancel");

	Binder<Ingredient> binder = new BeanValidationBinder<>(Ingredient.class);
	private Ingredient ingredient;

	public IngredientForm() {
		addClassName("ingredient-form");

		binder.bindInstanceFields(this);

		add(
				name,
				description,
				createButtonsLayout()
		);
	}

	public void setIngredient(Ingredient ingredient) {
		this.ingredient = ingredient;
		binder.readBean(ingredient);
	}

	private Component createButtonsLayout() {
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
		close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

		save.addClickShortcut(Key.ENTER, KeyModifier.CONTROL);
		close.addClickShortcut(Key.ESCAPE);

		save.addClickListener(click -> validateAndSave());
		delete.addClickListener(click -> fireEvent(new DeleteEvent(this, ingredient)));
		close.addClickListener(click -> fireEvent(new CloseEvent(this)));

		binder.addStatusChangeListener(evt -> save.setEnabled(binder.isValid()));

		return new HorizontalLayout(save, delete, close);
	}

	private void validateAndSave() {

		try {
			binder.writeBean(ingredient);
			fireEvent(new SaveEvent(this, ingredient));
		} catch (ValidationException e) {
			e.printStackTrace();
		}
	}

	// Events
	public static abstract class IngredientFormEvent extends ComponentEvent<IngredientForm> {
		private final Ingredient ingredient;

		protected IngredientFormEvent(IngredientForm source, Ingredient ingredient) {
			super(source, false);
			this.ingredient = ingredient;
		}

		public Ingredient getIngredient() {
			return ingredient;
		}
	}

	public static class SaveEvent extends IngredientFormEvent {
		SaveEvent(IngredientForm source, Ingredient ingredient) {
			super(source, ingredient);
		}
	}

	public static class DeleteEvent extends IngredientFormEvent {
		DeleteEvent(IngredientForm source, Ingredient ingredient) {
			super(source, ingredient);
		}

	}

	public static class CloseEvent extends IngredientFormEvent {
		CloseEvent(IngredientForm source) {
			super(source, null);
		}
	}

	public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
	                                                              ComponentEventListener<T> listener) {
		return getEventBus().addListener(eventType, listener);
	}

}
