package com.kodilla.airporthater.views;

import com.kodilla.airporthater.domain.dto.AirportDto;
import com.kodilla.airporthater.layout.MainLayout;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Route(value = "airports", layout = MainLayout.class)
public class AirportListView extends VerticalLayout {

    private final RestTemplate restTemplate;

    @Autowired
    public AirportListView(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        add(new H1("Airports:"));
        loadAirports();
        addSearchFieldAndButton();
    }

    private void loadAirports() {
        List<AirportDto> airports = Arrays.asList(restTemplate.getForObject("http://localhost:8080/airport", AirportDto[].class));
        Grid<AirportDto> grid = new Grid<>(AirportDto.class);
        grid.setItems(airports);
        grid.removeColumnByKey("commentIds");

        grid.setColumns("airportScore", "city", "name", "iataCode", "icaoCode");

        grid.addComponentColumn(this::createDetailsButton).setHeader("Action");

        add(grid);
    }

    private Button createDetailsButton(AirportDto airport) {
        Button detailsButton = new Button("Details");
        detailsButton.addClickListener(event -> {
            getUI().ifPresent(ui -> ui.navigate(AirportDetailsView.class, airport.getIataCode()));
        });
        return detailsButton;
    }

    private void addSearchFieldAndButton() {
        TextField iataCodeField = new TextField("Enter IATA (uppercase)");
        Button searchButton = new Button("Search and Save");
        searchButton.addClickListener(event -> {
            String iataCode = iataCodeField.getValue();
            if (!iataCode.isEmpty()) {
                restTemplate.postForObject("http://localhost:8080/airport/saveNewAirport/{iataCode}", null, Void.class, iataCode);
                loadAirports();
            } else {
                Notification.show("Please enter an IATA code", 3000, Notification.Position.TOP_CENTER);
            }
        });
        add(new Text("If the airport is not on the page - add it to our database!"));
        add(iataCodeField, searchButton);
    }
}
