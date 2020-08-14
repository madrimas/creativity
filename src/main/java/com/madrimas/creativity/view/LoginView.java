package com.madrimas.creativity.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = LoginView.ROUTE)
@PageTitle("Login")
public class LoginView extends VerticalLayout {
	public static final String ROUTE = "login";

	public LoginView() {
		add(new Button("Click me", e -> Notification.show("Hello, Spring+Vaadin user!")));

		LoginForm component = new LoginForm();
		component.addLoginListener(e -> {
			boolean isAuthenticated = true;
//			boolean isAuthenticated = authenticate(e);
			if (isAuthenticated) {
				navigateToMainPage();
			} else {
				component.setError(true);
			}
		});

		add(component);
	}

	private void navigateToMainPage() {

	}

}
