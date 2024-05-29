package com.kodilla.airporthater.views;

import com.kodilla.airporthater.layout.MainLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.router.Route;

@Route(value = "", layout = MainLayout.class)
public class HomeView extends Div {
    public HomeView() {
        H1 welcomeHeader = new H1("Welcome to the Airport Hater App!\n");
        welcomeHeader.getStyle().set("text-align", "center");
        add(welcomeHeader);

        H2 subtitleHeader = new H2("If airports are your nemesis, you're in the right place.\n");
        subtitleHeader.getStyle().set("text-align", "center");
        add(subtitleHeader);

        H3 subtitle = new H3("Let's join forces and rate airports together!");
        subtitle.getStyle().set("text-align", "center");
        add(subtitle);
    }
}
