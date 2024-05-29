package com.kodilla.airporthater.views;

import com.kodilla.airporthater.layout.MainLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.router.Route;

@Route(value = "", layout = MainLayout.class)
public class HomeView extends Div {
    public HomeView() {
        H1 welcomeHeader = new H1("Welcome to Airport Hater App!\n");
        welcomeHeader.getStyle().set("text-align", "center");
        add(welcomeHeader);

        H3 subtitle = new H3("Let's rate airports together!");
        subtitle.getStyle().set("text-align", "center");
        add(subtitle);
    }
}
