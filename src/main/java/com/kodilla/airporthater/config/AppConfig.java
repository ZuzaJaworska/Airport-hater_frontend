package com.kodilla.airporthater.config;

import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Value;

@SpringComponent
public class AppConfig {

    @Value("${backend.url}")
    private String backendUrl;

    public String getBackendUrl() {
        return backendUrl;
    }
}