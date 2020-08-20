package com.madrimas.creativity.ui.view;

import com.madrimas.creativity.controller.UserController;
import com.madrimas.creativity.service.UserService;
import com.madrimas.creativity.ui.MainLayout;
import com.madrimas.creativity.ui.UserForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value="user", layout = MainLayout.class)
@PageTitle("Users | Creativity")
public class UserView extends HorizontalLayout {

	private final UserForm form;

	private final UserController userController;

	private final UserService userService;

	public UserView(UserController userController, UserService userService){
		this.userController = userController;
		this.userService = userService;

		form = new UserForm(userService.getCurrentUser());
		form.addListener(UserForm.SaveEvent.class, this::saveUser);

		add(form);
	}

	private void saveUser(UserForm.SaveEvent event) {
		userController.updateUser(event.getUser());
		Notification.show("User has been updated");
	}
}
