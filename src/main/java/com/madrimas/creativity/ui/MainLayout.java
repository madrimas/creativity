package com.madrimas.creativity.ui;

import com.madrimas.creativity.ui.view.IngredientsView;
import com.madrimas.creativity.ui.view.RecipesView;
import com.madrimas.creativity.ui.view.UserView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;

@CssImport("./styles/shared-styles.css")
public class MainLayout extends AppLayout {

	public MainLayout() {
		createHeader();
		createDrawer();
	}

	private void createHeader() {
		H1 logo = new H1("Creativity");

		Anchor logout = new Anchor("logout", "Log out");

		HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, logout);
		header.expand(logo);
		header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
		header.setWidth("100%");
		header.addClassName("header");

		addToNavbar(header);
	}

	private void createDrawer() {
		RouterLink listLink = new RouterLink("List", RecipesView.class);
		listLink.setHighlightCondition(HighlightConditions.sameLocation());
		RouterLink userLink = new RouterLink("User", UserView.class);
		userLink.setHighlightCondition(HighlightConditions.sameLocation());
		RouterLink ingredientsLink = new RouterLink("Ingredients", IngredientsView.class);
		userLink.setHighlightCondition(HighlightConditions.sameLocation());

		addToDrawer(new VerticalLayout(listLink));
		addToDrawer(new VerticalLayout(userLink));
		addToDrawer(new VerticalLayout(ingredientsLink));
	}
}
