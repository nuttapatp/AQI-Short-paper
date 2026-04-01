package com.api.des.service;

import com.api.des.client.OpenWeatherMapClient;
import com.api.des.model.PollutionData;
import com.api.des.ultis.UtilityMethods;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.QueryJobConfiguration;
import com.google.cloud.bigquery.TableResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import static com.api.des.controller.PollutionDataController.logger;

@Service
public class PollutionDataService {
    private final BigQuery bigQuery;
    private static final Map<String, double[]> CITY_COORDINATES = new HashMap<>();

    static {
        CITY_COORDINATES.put("Bangkok", new double[]{13.7563, 100.5018});
        CITY_COORDINATES.put("Chiang Mai", new double[]{18.7883, 98.9853});
        CITY_COORDINATES.put("Chonburi", new double[]{13.3611, 100.9847});
        CITY_COORDINATES.put("Phuket", new double[]{7.8804, 98.3923});
    }
    @Autowired
    private OpenWeatherMapClient openWeatherMapClient;

    @Autowired
    public PollutionDataService(BigQuery bigQuery, OpenWeatherMapClient openWeatherMapClient) {

        this.bigQuery = bigQuery;
        this.openWeatherMapClient = openWeatherMapClient;

    }

    public int getCurrentAQI(double lat, double lon) {
        PollutionData data = openWeatherMapClient.fetchCurrentPollutionData(lat, lon);
        double pm25 = data.getList().get(0).getComponents().getPm2_5(); // Fetch PM2.5 value
        return UtilityMethods.convertPM25ToAQI(pm25); // Convert PM2.5 to AQI
    }


    public PollutionData getForecastData(double lat, double lon) {
        return openWeatherMapClient.fetchForecastPollutionData(lat, lon);
    }



    public void fetchCurrentDataAndStoreInBigQuery(String city) {
        double[] coordinates = CITY_COORDINATES.get(city);
        if (coordinates != null) {
            logger.debug("Fetching current AQI for city: {} at [{}, {}]", city, coordinates[0], coordinates[1]);
            int aqi = getCurrentAQI(coordinates[0], coordinates[1]);
            logger.debug("AQI fetched for {}: {}", city, aqi);
            long timestamp = System.currentTimeMillis() / 1000;

            storeDataInBigQuery(aqi, coordinates[0], coordinates[1], timestamp, city, "currentaqi");

            if (isDataStoredSuccessfully(city, timestamp, "currentaqi")) {
                logger.info("Current AQI for {} stored in BigQuery: {}", city, aqi);
            } else {
                logger.warn("Failed to store data for city {} in BigQuery.", city);
            }
        } else {
            logger.warn("Unknown city requested: {}", city);
        }
    }

    private boolean isDataStoredSuccessfully(String city, long timestamp, String tableName) {
        // Query BigQuery to check if data was stored successfully
        return !isDuplicateEntry("currentapi", tableName, city, timestamp);
    }

    public void fetchAllCurrentDataAndStoreInBigQuery() {
        logger.info("Fetching current AQI for all {} cities", CITY_COORDINATES.size());
        CITY_COORDINATES.forEach((city, coordinates) -> {
            int aqi = getCurrentAQI(coordinates[0], coordinates[1]);
            logger.debug("AQI for {}: {}", city, aqi);
            long timestamp = System.currentTimeMillis() / 1000;
            storeDataInBigQuery(aqi, coordinates[0], coordinates[1], timestamp, city, "currentaqi");
        });
    }

    public void fetchAllForecastDataAndStoreInBigQuery() {
        CITY_COORDINATES.forEach((city, coordinates) -> {
            PollutionData forecastData = getForecastData(coordinates[0], coordinates[1]);
            forecastData.getList().forEach(forecast -> {
                double pm25 = forecast.getComponents().getPm2_5();
                int aqi = UtilityMethods.convertPM25ToAQI(pm25);
                long timestamp = forecast.getDt();
                if (!isDuplicateEntry("currentapi", "forecastaqi", city, timestamp)) {
                    storeDataInBigQuery(aqi, coordinates[0], coordinates[1], timestamp, city, "forecastaqi");
                }

            });
        });
    }
    private boolean isDuplicateEntry(String datasetName, String tableName, String city, long timestamp) {
        String query = String.format(
                "SELECT COUNT(*) FROM %s.%s WHERE city = '%s' AND timestamp = '%s'",
                datasetName, tableName, city, convertUnixTimeToTimestamp(timestamp)
        );

        QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query).build();
        try {
            TableResult result = bigQuery.query(queryConfig);
            long count = result.iterateAll().iterator().next().get(0).getLongValue();
            return count > 0;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            // Handle exception
            return false;
        }
    }


    public void fetchForecastDataAndStoreInBigQuery(String city) {
        double[] coordinates = CITY_COORDINATES.get(city);
        logger.debug("Fetching forecast data for city: {}", city);
        if (coordinates != null) {
            PollutionData forecastData = getForecastData(coordinates[0], coordinates[1]);
            forecastData.getList().forEach(forecast -> {
                double pm25 = forecast.getComponents().getPm2_5();
                int aqi = UtilityMethods.convertPM25ToAQI(pm25);
                long timestamp = forecast.getDt();
                if (!isDuplicateEntry("currentapi", "forecastaqi", city, timestamp)) {
                    storeDataInBigQuery(aqi, coordinates[0], coordinates[1], timestamp, city, "forecastaqi");
                } else {
                    logger.debug("Skipping duplicate forecast entry for {} at timestamp {}", city, timestamp);
                }
            });
            logger.info("Forecast data stored for city: {}", city);
        } else {
            logger.warn("Unknown city requested for forecast: {}", city);
        }
    }

    @Scheduled(cron = "0 0 * * * *") // Every hour
    public void scheduledFetchAllCities() {
        logger.info("Scheduled AQI fetch started for all cities");
        fetchAllCurrentDataAndStoreInBigQuery();
        fetchAllForecastDataAndStoreInBigQuery();
        logger.info("Scheduled AQI fetch completed");
    }

//    public void testSaveDataToBigQuery() {
//        // Use hardcoded data or fetch from somewhere
//        int aqi = 50; // Example AQI value
//        double lat = 13.7563; // Example latitude
//        double lon = 100.5018; // Example longitude
//        long timestamp = System.currentTimeMillis() / 1000; // Current UNIX timestamp
//
//        storeDataInBigQuery(aqi, lat, lon, timestamp);
//    }

    private String convertUnixTimeToTimestamp(long unixTime) {
        Date date = new Date(unixTime * 1000L); // converting seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC")); // set the timezone as needed
        return sdf.format(date);
    }
    public void storeDataInBigQuery(int aqi, double lat, double lon, long timestamp, String city, String tableName) {
        String datasetName = "currentapi";
        String formattedTimestamp = convertUnixTimeToTimestamp(timestamp);

        String query = String.format(
                "INSERT INTO %s.%s (city, aqi, latitude, longitude, timestamp) VALUES ('%s', %d, %f, %f, '%s')",
                datasetName, tableName, city, aqi, lat, lon, formattedTimestamp
        );

        QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query).build();

        try {
            bigQuery.query(queryConfig);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            // Handle exception
        }
    }}
