package com.kodilla.airporthater.layout;

import com.kodilla.airporthater.views.HomeView;
import com.kodilla.airporthater.views.UserCreationView;
import com.kodilla.airporthater.views.UserListView;
import com.kodilla.airporthater.views.AirportListView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class MainLayout extends Div implements RouterLayout {
    public MainLayout() {
        HorizontalLayout menuLayout = new HorizontalLayout();

        RouterLink homeLink = new RouterLink("Home", HomeView.class);
        RouterLink createUserLink = new RouterLink("Create user", UserCreationView.class);
        RouterLink userListLink = new RouterLink("Users", UserListView.class);
        RouterLink airportsLink = new RouterLink("Airports", AirportListView.class);

        menuLayout.add(homeLink, createUserLink, userListLink, airportsLink);
        add(menuLayout);
    }
}
