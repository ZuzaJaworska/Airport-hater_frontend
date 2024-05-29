package com.kodilla.airporthater.views;

import com.kodilla.airporthater.domain.dto.AirportDto;
import com.kodilla.airporthater.domain.dto.WeatherResponseDto;
import com.kodilla.airporthater.layout.MainLayout;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

@Route(value = "airport/iata", layout = MainLayout.class)
public class AirportDetailsView extends Div implements HasUrlParameter<String> {

    private final RestTemplate restTemplate;

    @Value("${backend.url}")
    private String apiUrl;

    public AirportDetailsView(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void setParameter(BeforeEvent event, String iataCode) {
        String url = apiUrl + "/airport/iata/" + iataCode;
        try {
            AirportDto airportDto = restTemplate.getForObject(url, AirportDto.class);
            if (airportDto != null) {
                VerticalLayout layout = new VerticalLayout();
                layout.add(new H1("Airport Details:"));

                Grid<AirportDto> grid = new Grid<>(AirportDto.class, false);
                grid.addColumn(AirportDto::getIataCode).setHeader("IATA Code");
                grid.addColumn(AirportDto::getIcaoCode).setHeader("ICAO Code");
                grid.addColumn(AirportDto::getName).setHeader("Airport Name");
                grid.addColumn(AirportDto::getCity).setHeader("City");
                grid.addColumn(AirportDto::getAirportScore).setHeader("Airport Score");

                grid.setItems(airportDto);

                layout.add(grid);

                Button commentsButton = new Button("View Comments", event1
                        -> getUI().ifPresent(ui -> ui.navigate(AirportCommentsView.class, iataCode)));
                layout.add(grid, commentsButton);

                Button weatherButton = new Button("View Weather at this Airport", event1 -> showWeather(iataCode));
                layout.add(weatherButton);

                Button deleteButton = new Button("Delete Airport", event2 -> {
                    String deleteUrl = apiUrl + "/airport/" + iataCode;
                    restTemplate.delete(deleteUrl);
                    // Redirect user to AirportListView page after successfully deleting the airport
                    getUI().ifPresent(ui -> ui.navigate(AirportListView.class));
                });
                layout.add(deleteButton);

                RouterLink backToListLink = new RouterLink("Back to Airport List", AirportListView.class);
                layout.add(backToListLink);

                add(layout);
            } else {
                add(new Text("Airport with IATA code not found: " + iataCode));
            }
        } catch (Exception e) {
            add(new Text("An error occurred while fetching data for IATA code: " + iataCode));
        }
    }

    private void showWeather(String iataCode) {
        String weatherUrl = apiUrl + "/airport/weather/" + iataCode;
        try {
            WeatherResponseDto weatherResponse = restTemplate.getForObject(weatherUrl, WeatherResponseDto.class);
            if (weatherResponse != null) {
                VerticalLayout weatherLayout = new VerticalLayout();
                weatherLayout.add(new H3("Weather at the Airport:"));

                Grid<WeatherResponseDto> weatherGrid = new Grid<>(WeatherResponseDto.class, false);
                weatherGrid.addColumn(WeatherResponseDto::getCity).setHeader("City");
                weatherGrid.addColumn(WeatherResponseDto::getWeather).setHeader("Weather");
                weatherGrid.addColumn(WeatherResponseDto::getTemperature).setHeader("Temperature");
                weatherGrid.addColumn(WeatherResponseDto::getPressure).setHeader("Pressure");
                weatherGrid.setItems(weatherResponse);

                weatherLayout.add(weatherGrid);

                add(weatherLayout);
            } else {
                add(new Text("Weather data not found for IATA code: " + iataCode));
            }
        } catch (Exception e) {
            add(new Text("An error occurred while fetching weather data for IATA code: " + iataCode));
        }
    }
}
