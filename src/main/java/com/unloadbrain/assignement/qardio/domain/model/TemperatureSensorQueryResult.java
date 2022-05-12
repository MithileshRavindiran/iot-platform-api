package com.unloadbrain.assignement.qardio.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Domain model class to read persisted data from InfluxDB.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Measurement(name = "TemperatureSensor")
public class TemperatureSensorQueryResult {

    @JsonIgnore
    @Column(name = "time")
    private Instant time;

    @Column(name = "temperatureInFahrenheit")
    private double temperatureInFahrenheit;
}