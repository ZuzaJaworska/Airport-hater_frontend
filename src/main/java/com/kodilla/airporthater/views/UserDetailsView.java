package com.kodilla.airporthater.views;

import com.kodilla.airporthater.domain.dto.CommentDto;
import com.kodilla.airporthater.domain.dto.UserDto;
import com.kodilla.airporthater.layout.MainLayout;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Route(value = "user-details", layout = MainLayout.class)
public class UserDetailsView extends VerticalLayout implements BeforeEnterObserver, HasUrlParameter<Long>, AfterNavigationObserver {

    private final RestTemplate restTemplate;
    private Long userId;

    private Grid<CommentDto> commentsGrid = new Grid<>(CommentDto.class);

    @Autowired
    public UserDetailsView(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        add(new H1("User details:"));
        configureCommentsGrid();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // This method is now empty
    }

    @Override
    public void setParameter(BeforeEvent event, Long parameter) {
        this.userId = parameter;
        if (userId != null && userId > 0) {
            UserDto user = restTemplate.getForObject("http://localhost:8080/user/" + userId, UserDto.class);
            if (user != null) {
                addUserInfo(user);
                H3 commentsTitle = new H3("User comments:");
                add(commentsTitle);
                List<CommentDto> comments = Arrays.asList(restTemplate.getForObject("http://localhost:8080/comment/user/" + userId, CommentDto[].class));
                commentsGrid.setItems(comments);
                add(commentsGrid);

                createButtons(user);
            } else {
                add(new Div(new Text("User not found")));
            }
        } else {
            add(new Div(new Text("User ID not provided")));
        }
    }

    private void configureCommentsGrid() {
        commentsGrid.setColumns("airportId", "score", "title", "body", "createdAt", "id");
    }

    private void addUserInfo(UserDto user) {
        VerticalLayout userInfoLayout = new VerticalLayout();
        Div userIdDiv = new Div();
        userIdDiv.setText("User ID: " + user.getId());
        Div usernameDiv = new Div();
        usernameDiv.setText("Username: " + user.getUsername());
        Div passwordDiv = new Div();
        passwordDiv.setText("Password: " + user.getPassword());
        Div blockedDiv = new Div();
        blockedDiv.setText("Blocked: " + user.isBlocked());

        userInfoLayout.add(
                userIdDiv,
                usernameDiv,
                passwordDiv,
                blockedDiv
        );
        add(userInfoLayout);
    }

    private void createButtons(UserDto user) {
        VerticalLayout buttonLayout = new VerticalLayout();
        Button blockButton = new Button("Block user", event -> {
            try {
                restTemplate.put("http://localhost:8080/user/block/" + user.getId(), null);
                Notification.show("User blocked successfully", 3000, Notification.Position.BOTTOM_CENTER);
            } catch (Exception e) {
                Notification.show("Failed to block user", 3000, Notification.Position.BOTTOM_CENTER);
            }
        });

        Button unblockButton = new Button("Unblock user", event -> {
            try {
                restTemplate.put("http://localhost:8080/user/unblock/" + user.getId(), null);
                Notification.show("User unblocked successfully", 3000, Notification.Position.BOTTOM_CENTER);
            } catch (Exception e) {
                Notification.show("Failed to unblock user", 3000, Notification.Position.BOTTOM_CENTER);
            }
        });

        Button deleteButton = new Button("Delete user", event -> {
            try {
                restTemplate.delete("http://localhost:8080/user/" + user.getId());
                Notification.show("User deleted successfully", 3000, Notification.Position.BOTTOM_CENTER);

                UI.getCurrent().navigate(UserListView.class);
            } catch (Exception e) {
                Notification.show("Failed to delete user", 3000, Notification.Position.BOTTOM_CENTER);
            }
        });

        buttonLayout.add(blockButton, unblockButton, deleteButton);
        add(buttonLayout);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        // The following code will be executed after navigating to this page
        // Check if the user has been deleted by checking if userId is not null
        if (userId == null) {
            // If userId is null, it means the user has been deleted
            // Redirect the user to the UserListView page
            UI.getCurrent().navigate(UserListView.class);
        }
    }
}
