package com.api.des;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.beans.factory.annotation.Value;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class BigQueryConfig {

    @Bean
    public BigQuery bigQuery(@Value("${BIGQUERY_CREDENTIALS:}") String credentialsJson) throws IOException {
        InputStream inputStream;

        if (credentialsJson != null && !credentialsJson.isBlank()) {
            // Render: use JSON string from env var
            inputStream = new ByteArrayInputStream(credentialsJson.getBytes());
        } else {
            // Local: use file from classpath
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