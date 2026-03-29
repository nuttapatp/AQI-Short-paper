package com.api.des.client;

import com.api.des.model.PollutionData;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OpenWeatherMapClient {

    private final RestTemplate restTemplate ;
    private final String apiKey = ""; // Ideally from properties

    public OpenWeatherMapClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Constructor, etc.

    public PollutionData fetchCurrentPollutionData(double lat, double lon) {
        String url = "http://api.openweathermap.org/data/2.5/air_pollution?lat=" + lat + "&lon=" + lon + "&appid=" + apiKey;
        return restTemplate.getForObject(url, PollutionData.class);
    }

    public PollutionData fetchForecastPollutionData(double lat, double lon) {
        String url = "http://api.openweathermap.org/data/2.5/air_pollution/forecast?lat=" + lat + "&lon=" + lon + "&appid=" + apiKey;
        return restTemplate.getForObject(url, PollutionData.class);
    }
}
