package com.madrimas.creativity.ui.view;

import com.madrimas.creativity.ui.MainLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value="", layout = MainLayout.class)
@PageTitle("Recipes | Creativity")
public class RecipesView extends VerticalLayout {

	public RecipesView() {

	}
}
