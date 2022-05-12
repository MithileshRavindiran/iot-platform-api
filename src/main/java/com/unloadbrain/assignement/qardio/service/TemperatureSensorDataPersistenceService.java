package com.unloadbrain.assignement.qardio.service;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.unloadbrain.assignement.qardio.dto.message.TemperatureSensorDataMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


/**
 * Listen to TrackTemperature Apache Kafka topic and persist to InfluxDB.
 */
@Slf4j
@Service
public class TemperatureSensorDataPersistenceService {

    private InfluxDBClient influxDB;

    public TemperatureSensorDataPersistenceService(InfluxDBClient influxDB) {
        this.influxDB = influxDB;
    }

    /**
     * Persist temperature data to InfluxDB.
     *
     * @param message the Apache Kafka message
     */
    @KafkaListener(topics = "TrackTemperature", groupId = "iot")
    public void trackTemperatureMessageListener(TemperatureSensorDataMessage message) {

        log.info("Received message {} TrackTemperature topic from group 'iot'", message);

        Point point = Point.measurement("TemperatureSensor")
                .time(message.getUnixTimestamp() * 1000L, WritePrecision.MS)
                .addField("deviceId", message.getDeviceId())
                .addField("temperatureInFahrenheit", message.getTemperatureInFahrenheit());

        influxDB.getWriteApiBlocking().writePoint(point);
    }


}
