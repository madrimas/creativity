package com.madrimas.creativity.ui.view;

import com.madrimas.creativity.controller.UserController;
import com.madrimas.creativity.ui.RegisterForm;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import static com.vaadin.flow.component.notification.Notification.Position.TOP_CENTER;

@Route(value = RegisterView.ROUTE)
@PageTitle("Register | Creativity")
public class RegisterView extends HorizontalLayout {

	public static final String ROUTE = "register";

	private final RegisterForm form;

	private final UserController userController;

	public RegisterView(UserController userController) {
		this.userController = userController;

		form = new RegisterForm();
		form.addListener(RegisterForm.CreateEvent.class, this::addUser);

		add(form);

		addBackToLoginButton();
	}

	private void addUser(RegisterForm.CreateEvent event) {
		userController.addUser(event.getUser());
		UI.getCurrent().navigate(LoginView.ROUTE);

		showSuccessMessage();
	}

	private void showSuccessMessage() {
		Span successMessage = new Span("User has been created. Please log in");
		successMessage.getStyle().set("color", "var(--lumo-success-text-color");
		Notification successNotification = new Notification(successMessage);
		successNotification.setDuration(5000);
		successNotification.setPosition(TOP_CENTER);
		successNotification.open();
	}

	private void addBackToLoginButton() {
		Button button = new Button("Back to log in page");
		button.getElement().setAttribute("aria-label", "Back to log in page");

		button.addClickListener(this::backToLogin);
		add(button);
	}

	private void backToLogin(ClickEvent<Button> buttonClickEvent) {
		UI.getCurrent().navigate(LoginView.ROUTE);
	}
}
