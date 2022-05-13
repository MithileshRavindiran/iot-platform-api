package com.unloadbrain.assignement.qardio.domain.repository;

import com.influxdb.client.InfluxDBClient;
import com.unloadbrain.assignement.qardio.domain.model.TemperatureSensorQueryResult;
import com.unloadbrain.assignement.qardio.exception.DataAccessException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TemperatureSensorDataRepositoryTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private InfluxDBClient influxDBMock;
    private TemperatureSensorDataRepository repository;

    @Before
    public void setUp() throws Exception {
        this.influxDBMock = mock(InfluxDBClient.class);
        this.repository = new TemperatureSensorDataRepository(influxDBMock);
    }

    @Test
    public void shouldReturnQueryResultList() {

        // Given


        when(influxDBMock.getQueryApi().query(any(String.class))).thenReturn(null);

        // When
        List<TemperatureSensorQueryResult> temperatureSensorQueryResultList
                = repository.getTemperatures("1234", 1563142796L, 1563142800L);

        // Then

        assertEquals(2, temperatureSensorQueryResultList.size());

        assertEquals("2019-07-14T22:19:58Z", temperatureSensorQueryResultList.get(0).getTime().toString());
        assertEquals(20.0, temperatureSensorQueryResultList.get(0).getValue(), 1e-15);

        assertEquals("2019-07-14T22:19:59Z", temperatureSensorQueryResultList.get(1).getTime().toString());
        assertEquals(21.0, temperatureSensorQueryResultList.get(1).getValue(), 1e-15);
    }

    @Test
    public void shouldThrowExceptionWhenQueryResultHasError() {

        // Given

        thrown.expect(DataAccessException.class);
        thrown.expectMessage("Could not access influxDB because of");

        // When
        repository.getTemperatures("1234", 1563142796L, 1563142800L);

        // Then
        // Expect test to be passed.
    }
}