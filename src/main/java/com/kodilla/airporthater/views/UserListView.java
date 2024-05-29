package com.kodilla.airporthater.views;

import com.kodilla.airporthater.domain.dto.UserDto;
import com.kodilla.airporthater.layout.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Route(value = "users", layout = MainLayout.class)
public class UserListView extends VerticalLayout {

    private final RestTemplate restTemplate;

    @Autowired
    public UserListView(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        add(new H1("Users:"));
        add(createUserGrid());
    }

    private Grid<UserDto> createUserGrid() {
        List<UserDto> users = Arrays.asList(restTemplate.getForObject("http://localhost:8080/user", UserDto[].class));
        Grid<UserDto> grid = new Grid<>(UserDto.class);
        grid.setItems(users);
        grid.removeColumnByKey("commentIds");
        grid.setColumns("id", "username", "password", "blocked");

        grid.addColumn(new ComponentRenderer<>(user -> {
            Button detailsButton = new Button("View Details", click -> {
                getUI().ifPresent(ui -> ui.navigate(UserDetailsView.class, user.getId()));
            });
            return detailsButton;
        })).setHeader("Actions");

        return grid;
    }
}
