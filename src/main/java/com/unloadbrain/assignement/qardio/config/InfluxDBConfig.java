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
    private final String username;
    private final String password;
    private final String database;
    private final String retentionPolicy;
    private final long readTimeout;

    public InfluxDBConfig(@Value("${app.influxdb.url}") String url,
                          @Value("${app.influxdb.user}") String username,
                          @Value("${app.influxdb.pass}") String password,
                          @Value("${app.influxdb.database}") String database,
                          @Value("${app.influxdb.retention-policy}") String retentionPolicy,
                          @Value("${app.influxdb.read-timeout-in-seconds}") long readTimeout) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.database = database;
        this.retentionPolicy = retentionPolicy;
        this.readTimeout = readTimeout;
    }

    @Bean
    public InfluxDBClient influxDB() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder().readTimeout(readTimeout, TimeUnit.SECONDS);
        InfluxDBClient influxDBClient = InfluxDBClientFactory.create(url, "ffzQs8mqsyG50Owsrpk5_kES9SGb_t24RV2jbJZI0Q6i0I9cIqFutDrYJAgBmJX3CPbHkn3wym-A8vvPNXAQjw==".toCharArray(), "Personal" ,"sensordata");


        //influxDBClient.getQueryApi().query("CREATE DATABASE " + database);

        influxDBClient.setLogLevel(LogLevel.BASIC);

        HealthCheck response = influxDBClient.health();
        if ("unknown".equalsIgnoreCase(response.getVersion())) {
            log.error("Error pinging InfluxDB server.");
        }

        return influxDBClient;
    }


}
