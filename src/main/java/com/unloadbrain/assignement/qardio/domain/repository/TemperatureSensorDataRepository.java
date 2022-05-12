package com.unloadbrain.assignement.qardio.domain.repository;

import com.influxdb.client.InfluxDBClient;
import com.unloadbrain.assignement.qardio.domain.model.TemperatureSensorQueryResult;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Data repository class to perform actions (e.g. fetch data) on temperature sensor data from InfluxDB.
 */
@Component
public class TemperatureSensorDataRepository {

    private final InfluxDBClient influxDB;

    public TemperatureSensorDataRepository(InfluxDBClient influxDB) {
        this.influxDB = influxDB;
    }

    /**
     * Gets temperature data filtered with device id, start and end time.
     *
     * @param deviceId  the device id.
     * @param startTime the start unix timestamp.
     * @param endTime   the end unix timestamp.
     * @return the list of TemperatureSensorQueryResult DTO.
     */
    public List<TemperatureSensorQueryResult> getTemperatures(String deviceId, long startTime, long endTime) {

        String query = buildQuery(deviceId, startTime, endTime);

        List<TemperatureSensorQueryResult> queryResult = influxDB.getQueryApi().query(query, TemperatureSensorQueryResult.class);
        return queryResult;
    }

    /**
     * Build query string from parameters. Note: InfluxDB query is SQL Injection safe.
     *
     * @param deviceId  device id
     * @param startTime start unix timestamp
     * @param endTime   end unix timestamp
     * @return query string with parameters
     * @see <a href="https://github.com/influxdata/influxdb-java/issues/274">Github issue related to SQL Injection</a>
     */
    private String buildQuery(String deviceId, long startTime, long endTime) {

        StringBuilder query = new StringBuilder();
        query.append("Select time, temperatureInFahrenheit from TemperatureSensor");
        query.append(" WHERE deviceId = '");
        query.append(deviceId);
        query.append('\'');
        query.append(" AND time >= ");
        query.append(startTime);
        query.append('s');
        query.append(" AND time <= ");
        query.append(endTime);
        query.append('s');

        return query.toString();
    }


}
