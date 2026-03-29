package com.api.des.config;

import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.Dataset;
import com.google.cloud.bigquery.DatasetInfo;

public class BigQueryTest {

    public static void testBigQueryConnection() {
        try {
            // Instantiate a client. If you don't specify credentials when constructing the client, the
            // client library will look for credentials via the environment variable.
            BigQuery bigQuery = BigQueryOptions.getDefaultInstance().getService();

            // Make a request to list datasets
            for (Dataset dataset : bigQuery.listDatasets().iterateAll()) {
                System.out.println(dataset.getDatasetId().getDataset());
            }

            System.out.println("BigQuery connection test completed successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to connect to BigQuery: " + e.getMessage());
        }
    }
}
