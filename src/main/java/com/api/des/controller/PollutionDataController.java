package com.api.des.controller;

import com.api.des.model.PollutionData;
import com.api.des.service.PollutionDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pollution")

public class PollutionDataController {


    @Autowired
    private PollutionDataService pollutionDataService;

    public static final Logger logger = LoggerFactory.getLogger(PollutionDataController.class);


    @GetMapping("/current")
    public ResponseEntity<?> getCurrentPollutionData(@RequestParam String city) {
        try {
            pollutionDataService.fetchCurrentDataAndStoreInBigQuery(city);
            return ResponseEntity.ok("Current AQI data for " + city + " fetched and stored in BigQuery");
        } catch (Exception e) {
            logger.error("Error fetching current AQI data: ", e);
            return ResponseEntity.internalServerError().body("Failed to fetch current AQI data: " + e.getMessage());
        }
    }

    @GetMapping("/forecast")
    public ResponseEntity<?> getForecastPollutionData(@RequestParam String city) {
        pollutionDataService.fetchForecastDataAndStoreInBigQuery(city);
        return ResponseEntity.accepted().body("Forecast data fetching initiated for " + city);
    }

    @GetMapping("/current/all")
    public ResponseEntity<?> getAllCurrentPollutionData() {
        try {
            pollutionDataService.fetchAllCurrentDataAndStoreInBigQuery();
            return ResponseEntity.ok("Current AQI data for all cities fetched and stored in BigQuery");
        } catch (Exception e) {
            logger.error("Error fetching current AQI data for all cities: ", e);
            return ResponseEntity.internalServerError().body("Failed to fetch current AQI data for all cities: " + e.getMessage());
        }
    }

    @GetMapping("/forecast/all")
    public ResponseEntity<?> getAllForecastPollutionData() {
        try {
            pollutionDataService.fetchAllForecastDataAndStoreInBigQuery();
            return ResponseEntity.ok("Forecast AQI data for all cities fetched and stored in BigQuery");
        } catch (Exception e) {
            logger.error("Error fetching forecast AQI data for all cities: ", e);
            return ResponseEntity.internalServerError().body("Failed to fetch forecast AQI data for all cities: " + e.getMessage());
        }
    }


    @GetMapping("/forecast/{city}")
    public ResponseEntity<?> getForecastPollutionDataForCity(@PathVariable String city) {
        try {
            pollutionDataService.fetchForecastDataAndStoreInBigQuery(city);

            return ResponseEntity.accepted().body("Forecast data fetching initiated for " + city);
        } catch (Exception e) {
            logger.error("Error fetching forecast AQI data for " + city + ": ", e);
            return ResponseEntity.internalServerError().body("Failed to fetch forecast AQI data for " + city + ": " + e.getMessage());
        }
    }



}
