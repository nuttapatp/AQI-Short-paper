package com.api.des.config;

import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.Dataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BigQueryTest {

    private static final Logger log = LoggerFactory.getLogger(BigQueryTest.class);

    public static void testBigQueryConnection() {
        try {
            BigQuery bigQuery = BigQueryOptions.getDefaultInstance().getService();

            for (Dataset dataset : bigQuery.listDatasets().iterateAll()) {
                log.debug("BigQuery dataset found: {}", dataset.getDatasetId().getDataset());
            }

            log.info("BigQuery connection test completed successfully");
        } catch (Exception e) {
            log.error("Failed to connect to BigQuery: {}", e.getMessage(), e);
        }
    }
}
