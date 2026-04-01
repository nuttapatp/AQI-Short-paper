package com.api.des;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class BigQueryConfig {
    @Bean
    public BigQuery bigQuery() throws IOException {
        String credentialsPath = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
        InputStream inputStream;

        if (credentialsPath != null && !credentialsPath.isEmpty()) {
            java.io.File file = new java.io.File(credentialsPath);
            if (file.exists()) {
                inputStream = new java.io.FileInputStream(file);
            } else {
                inputStream = getClass().getClassLoader()
                        .getResourceAsStream("air-quality-api-491405-7c36b2e10284.json");
            }
        } else {
            inputStream = getClass().getClassLoader()
                    .getResourceAsStream("air-quality-api-491405-7c36b2e10284.json");
        }

        if (inputStream == null) {
            throw new IOException("Could not find BigQuery credentials file!");
        }

        GoogleCredentials credentials = GoogleCredentials.fromStream(inputStream);
        return BigQueryOptions.newBuilder().setCredentials(credentials).build().getService();
    }
}