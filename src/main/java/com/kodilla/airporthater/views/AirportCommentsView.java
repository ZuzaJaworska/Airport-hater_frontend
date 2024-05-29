package com.kodilla.airporthater.views;

import com.kodilla.airporthater.domain.dto.CommentDto;
import com.kodilla.airporthater.layout.MainLayout;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Route(value = "airport/comments", layout = MainLayout.class)
public class AirportCommentsView extends VerticalLayout implements HasUrlParameter<String> {

    private final RestTemplate restTemplate;

    @Autowired
    public AirportCommentsView(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        add(new H1("Comments:"));
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String iataCode) {
        String url = "http://localhost:8080/comment/iata/" + iataCode;
        try {
            add(new Text("Comments for the airport with IATA code: " + iataCode));

            List<CommentDto> comments = Arrays.asList(restTemplate.getForObject(url, CommentDto[].class));
            Grid<CommentDto> grid = new Grid<>(CommentDto.class, false);
            grid.setItems(comments);
            grid.setColumns("title", "body", "score", "createdAt");

            add(grid);

            H3 addCommentTitle = new H3("Add Comment");
            add(addCommentTitle);

            FormLayout commentFormLayout = new FormLayout();
            TextField titleField = new TextField("Title");
            TextField bodyField = new TextField("Comment");
            TextField scoreField = new TextField("Score");
            TextField userIdField = new TextField("User ID");
            Button addButton = new Button("Add Comment");

            addButton.addClickListener(event -> {
                CommentDto newComment = new CommentDto();
                newComment.setTitle(titleField.getValue());
                newComment.setBody(bodyField.getValue());
                newComment.setScore(Integer.parseInt(scoreField.getValue()));
                newComment.setCreatedAt(LocalDateTime.now());
                newComment.setAirportId(iataCode);
                Long userId = Long.parseLong(userIdField.getValue());
                newComment.setUserId(userId);

                try {
                    restTemplate.postForObject("http://localhost:8080/comment", newComment, Void.class);
                    Notification.show("Comment added", 3000, Notification.Position.TOP_CENTER);
                    grid.setItems(Arrays.asList(restTemplate.getForObject(url, CommentDto[].class)));
                } catch (Exception ex) {
                    Notification.show("Error adding comment", 3000, Notification.Position.TOP_CENTER);
                }
            });

            commentFormLayout.addFormItem(titleField, "Title");
            commentFormLayout.addFormItem(bodyField, "Comment");
            commentFormLayout.addFormItem(scoreField, "Score");
            commentFormLayout.addFormItem(userIdField, "User ID");
            commentFormLayout.add(addButton);

            Button deleteButton = new Button("Delete selected comment");
            deleteButton.addClickListener(event -> {
                CommentDto selectedComment = grid.asSingleSelect().getValue();
                if (selectedComment != null) {
                    try {
                        restTemplate.delete("http://localhost:8080/comment/" + selectedComment.getId());
                        Notification.show("Comment deleted", 3000, Notification.Position.TOP_CENTER);
                        grid.setItems(Arrays.asList(restTemplate.getForObject(url, CommentDto[].class)));
                    } catch (Exception ex) {
                        Notification.show("Error deleting comment", 3000, Notification.Position.TOP_CENTER);
                    }
                } else {
                    Notification.show("Select a comment to delete", 3000, Notification.Position.TOP_CENTER);
                }
            });

            add(commentFormLayout, deleteButton);
        } catch (Exception e) {
            add(new Text("An error occurred while fetching comments for the airport with IATA code: " + iataCode));
        }
    }
}
