package com.kodilla.airporthater.views;

import com.kodilla.airporthater.domain.dto.UserDto;
import com.kodilla.airporthater.layout.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

@Route(value = "user-create", layout = MainLayout.class)
public class UserCreationView extends VerticalLayout {

    private final RestTemplate restTemplate;

    @Autowired
    public UserCreationView(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        add(createUserForm());
    }

    private FormLayout createUserForm() {
        TextField usernameField = new TextField("Username");
        PasswordField passwordField = new PasswordField("Password");

        Button createButton = new Button("Create User");
        createButton.addClickListener(event -> {
            String username = usernameField.getValue();
            String password = passwordField.getValue();
            if (!username.isEmpty() && !password.isEmpty()) {
                UserDto newUserDto = new UserDto();
                newUserDto.setUsername(username);
                newUserDto.setPassword(password);
                restTemplate.postForObject("http://localhost:8080/user", newUserDto, Void.class);
                showUserCreatedNotification(newUserDto);
            } else {
                Notification.show("Please fill in all fields", 3000, Notification.Position.BOTTOM_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });

        FormLayout formLayout = new FormLayout();
        formLayout.add(usernameField, passwordField, createButton);
        return formLayout;
    }

    private void showUserCreatedNotification(UserDto userDto) {
        Notification.show("User created successfully: " + userDto.getUsername() + "!\n Check \"Users\" tab and find your ID to add comments",
                        5000, Notification.Position.BOTTOM_CENTER)
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }
}
