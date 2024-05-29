package com.kodilla.airporthater.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class BackendService {

    @Value("${backend.url}")
    private String backendUrl;

    private final RestTemplate restTemplate;

    public BackendService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String callBackendApi() {
        String apiUrl = backendUrl;
        return restTemplate.getForObject(apiUrl, String.class);
    }
}
