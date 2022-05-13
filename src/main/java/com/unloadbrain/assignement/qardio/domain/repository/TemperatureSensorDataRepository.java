package com.unloadbrain.assignement.qardio.domain.repository;

import com.influxdb.client.InfluxDBClient;
import com.unloadbrain.assignement.qardio.domain.model.TemperatureSensorQueryResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Data repository class to perform actions (e.g. fetch data) on temperature sensor data from InfluxDB.
 */
@Component
public class TemperatureSensorDataRepository {

    private final InfluxDBClient influxDB;

    @Value("${app.influxdb.bucket}")
    String bucket;

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

        //String flux = "from(bucket:) |> range(start:0) |> filter(fn: (r) => r[\"_measurement\"] == \"sensor\") |> filter(fn: (r) => r[\"sensor_id\"] == \"TLM0100\"or r[\"sensor_id\"] == \"TLM0101\" or r[\"sensor_id\"] == \"TLM0103\" or r[\"sensor_id\"] == \"TLM0200\") |> sort() |> yield(name: \"sort\")";
        StringBuilder query = new StringBuilder();

        query.append("from(bucket:").append("\"").append(bucket).append("\")")
                .append(" |> range(start: ").append(startTime).append(",").append("stop:").append(endTime).append(")")
                .append(" |> filter(fn: (r) => r._measurement == \"TemperatureSensor\")")
                .append(" |> filter(fn: (r) => r._field == \"temperatureInFahrenheit\")")
                        .append("|> sort(columns: [\"_value\"], desc: true)");


        //query to sort for max and min

        /**
         *
         * from(bucket: "sensordata")
         *            |> range(start: 0)
         *            |> filter(fn: (r) => r._measurement == "TemperatureSensor")
         *            |> filter(fn: (r) => r._field == "temperatureInFahrenheit")
         *            |> sort(columns: ["_value"], desc: true)
         *
         */



        //query within time range
        /**
         *
         * from(bucket: "sensordata")
         *   |> range(start: 2022-05-12T20:49:59Z, stop: 2022-05-12T21:49:59Z)
         *   |> filter(fn: (r) => r._measurement == "TemperatureSensor")
         *   |> filter(fn: (r) => r._field == "temperatureInFahrenheit")
         *   |> sort(columns: ["_value"], desc: true)
         *
         */

        return query.toString();
    }


}
