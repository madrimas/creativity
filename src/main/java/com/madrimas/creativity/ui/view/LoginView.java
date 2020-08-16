package com.madrimas.creativity.ui.view;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = LoginView.ROUTE)
@PageTitle("Login")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

	public static final String ROUTE = "login";

	private final LoginForm login = new LoginForm();

	public LoginView() {
		prepareView();
		addLoginForm();
		addRegisterButton();
	}

	private void prepareView() {
		addClassName("login-view");
		setSizeFull();
		setAlignItems(Alignment.CENTER);
		setJustifyContentMode(JustifyContentMode.CENTER);
	}

	private void addLoginForm() {
		login.setForgotPasswordButtonVisible(false);
		login.setAction("login");
		add(new H1("Creativity"), login);
	}

	private void addRegisterButton() {
		Button button = new Button("Register");
		button.getElement().setAttribute("aria-label", "Register");

		button.addClickListener(this::register);
		add(button);
	}

	private void register(ClickEvent<Button> buttonClickEvent) {
		//TODO
	}

	@Override
	public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
		boolean hasErrors = beforeEnterEvent
				.getLocation()
				.getQueryParameters()
				.getParameters()
				.containsKey("error");

		if (hasErrors) {
			login.setError(true);
		}
	}
}
