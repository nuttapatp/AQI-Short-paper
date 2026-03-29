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
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("");
        GoogleCredentials credentials = GoogleCredentials.fromStream(inputStream);
        return BigQueryOptions.newBuilder().setCredentials(credentials).build().getService();
    }
}
