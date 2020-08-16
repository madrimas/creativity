package com.madrimas.creativity.ui.view;

import com.madrimas.creativity.controller.UserController;
import com.madrimas.creativity.model.User;
import com.madrimas.creativity.ui.MainLayout;
import com.madrimas.creativity.ui.UserForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Route(value="user", layout = MainLayout.class)
@PageTitle("Users | Creativity")
public class UserView extends HorizontalLayout {

	private final UserForm form;

	private final UserController userController;

	public UserView(UserController userController){
		this.userController = userController;

		form = new UserForm(getCurrentUser());
		form.addListener(UserForm.SaveEvent.class, this::saveUser);

		add(form);
	}

	private User getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			String currentUserName = authentication.getName();
			return userController.getUserByLogin(currentUserName);
		}

		return new User();
	}

	private void saveUser(UserForm.SaveEvent event) {
		userController.updateUser(event.getUser());
		Notification.show("User has been updated");
	}
}
