package com.unloadbrain.assignement.qardio.config;

import com.influxdb.LogLevel;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.domain.HealthCheck;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * This class provides InfluxDB related configuration and beans.
 */
@Slf4j
@Configuration
public class InfluxDBConfig {

    private final String url;
    private final String token;
    private final String organisation;
    private final String bucket;
    private final String retentionPolicy;
    private final long readTimeout;

    public InfluxDBConfig(@Value("${app.influxdb.url}") String url,
                          @Value("${app.influxdb.token}") String token,
                          @Value("${app.influxdb.org}") String organisation,
                          @Value("${app.influxdb.bucket}") String bucket,
                          @Value("${app.influxdb.retention-policy}") String retentionPolicy,
                          @Value("${app.influxdb.read-timeout-in-seconds}") long readTimeout) {
        this.url = url;
        this.token = token;
        this.organisation = organisation;
        this.bucket = bucket;
        this.retentionPolicy = retentionPolicy;
        this.readTimeout = readTimeout;
    }

    @Bean
    public InfluxDBClient influxDB() {

        InfluxDBClient influxDBClient = InfluxDBClientFactory.create(url, token.toCharArray(), organisation ,bucket);

        influxDBClient.setLogLevel(LogLevel.BASIC);

        HealthCheck response = influxDBClient.health();
        if ("unknown".equalsIgnoreCase(response.getVersion())) {
            log.error("Error pinging InfluxDB server.");
        }

        return influxDBClient;
    }


}
