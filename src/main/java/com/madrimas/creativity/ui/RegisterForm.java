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
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

public class RegisterForm extends FormLayout {

	TextField login = new TextField("Login");
	PasswordField password = new PasswordField("Password");
	EmailField email = new EmailField("Email");
	TextField firstName = new TextField("First name");
	TextField lastName = new TextField("Last name");

	Button create = new Button("Create");

	Binder<User> binder = new BeanValidationBinder<>(User.class);

	private final User newUser;

	public RegisterForm() {
		newUser = new User();
		addClassName("user-form");

		binder.forField(login)
				.bind(User::getLogin, User::setLogin);
		binder.forField(password)
				.bind(User::getPassword, User::setPassword);
		binder.forField(email)
				.bind(User::getEmail, User::setEmail);
		binder.forField(firstName)
				.bind(User::getFirstname, User::setFirstname);
		binder.forField(lastName)
				.bind(User::getLastname, User::setLastname);

		binder.readBean(newUser);

		add(login, password, email, firstName, lastName, createButtonsLayout());
	}

	private HorizontalLayout createButtonsLayout() {
		create.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		create.addClickShortcut(Key.ENTER);
		create.addClickListener(click -> validateAndCreate());

		binder.addStatusChangeListener(evt -> create.setEnabled(binder.isValid()));

		return new HorizontalLayout(create);
	}

	private void validateAndCreate() {
		try {
			binder.writeBean(newUser);
			fireEvent(new CreateEvent(this, newUser));
		} catch (ValidationException e) {
			e.printStackTrace();
		}
	}

	public static abstract class UserFormEvent extends ComponentEvent<RegisterForm> {
		private final User user;

		protected UserFormEvent(RegisterForm source, User user) {
			super(source, false);
			this.user = user;
		}

		public User getUser() {
			return user;
		}
	}

	public static class CreateEvent extends UserFormEvent {
		CreateEvent(RegisterForm source, User user) {
			super(source, user);
		}
	}

	public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
	                                                              ComponentEventListener<T> listener) {
		return getEventBus().addListener(eventType, listener);
	}
}
