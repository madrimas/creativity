package com.madrimas.creativity.ui;

import com.madrimas.creativity.model.User;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

public class UserForm extends FormLayout {
	TextField login = new TextField("Login");
	EmailField email = new EmailField("Email");
	TextField firstName = new TextField("First name");
	TextField lastName = new TextField("Last name");

	Button save = new Button("Save");

	Binder<User> binder = new BeanValidationBinder<>(User.class);

	private final User currentUser;

	public UserForm(User user) {
		currentUser = user;
		addClassName("user-form");

		binder.forField(login)
				.bind(User::getLogin, null);
		binder.forField(email)
				.bind(User::getEmail, User::setEmail);
		binder.forField(firstName)
				.bind(User::getFirstname, User::setFirstname);
		binder.forField(lastName)
				.bind(User::getLastname, User::setLastname);

		binder.readBean(currentUser);

		add(login, email, firstName, lastName, createButtonsLayout());
	}

	private HorizontalLayout createButtonsLayout() {
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		save.addClickShortcut(Key.ENTER);
		save.addClickListener(click -> validateAndSave());

		binder.addStatusChangeListener(evt -> save.setEnabled(binder.isValid()));

		return new HorizontalLayout(save);
	}

	private void validateAndSave() {
		try {
			binder.writeBean(currentUser);
			fireEvent(new SaveEvent(this, currentUser));
		} catch (ValidationException e) {
			e.printStackTrace();
		}
	}

	public static abstract class UserFormEvent extends ComponentEvent<UserForm> {
		private final User user;

		protected UserFormEvent(UserForm source, User user) {
			super(source, false);
			this.user = user;
		}

		public User getUser() {
			return user;
		}
	}

	public static class SaveEvent extends UserFormEvent {
		SaveEvent(UserForm source, User user) {
			super(source, user);
		}
	}

	public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
	                                                              ComponentEventListener<T> listener) {
		return getEventBus().addListener(eventType, listener);
	}
}
