package com.kyotu.largefilereadchallenge.unit;

import com.kyotu.largefilereadchallenge.exception.FileReadException;
import com.kyotu.largefilereadchallenge.model.data.calculation.YearlyAverage;
import com.kyotu.largefilereadchallenge.service.TemperatureService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TemperatureServiceTest {

    private final String correctFilePath = "src/test/resources/temp-measurements.csv";
    private final String wrongFilePath = "src/test/resources/test.csv";

    private TemperatureService service;

    @BeforeEach
    void setUp() {
        service = new TemperatureService();
    }

    @Test
    public void aggregation_shouldCalculateAverages_whenFileAndCityFound() {
        service.setInputFilePath(correctFilePath);

        List<YearlyAverage> result = service.findAverageCityTemperaturesWithAggregation("warszawa");

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(7);
    }

    @Test
    public void aggregation_shouldCalculateNoAverages_whenCityNotFound() {
        service.setInputFilePath(correctFilePath);

        List<YearlyAverage> result = service.findAverageCityTemperaturesWithAggregation("szczecin");

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void aggregation_throwException_whenFileNotFound() {
        service.setInputFilePath(wrongFilePath);

        Exception exception = Assertions.assertThrows(FileReadException.class,
                () -> service.findAverageCityTemperaturesWithAggregation("warszawa"));

        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains("Error reading file:"));
    }

    @Test
    public void stream_shouldCalculateAverages_whenFileAndCityFound() {
        service.setInputFilePath(correctFilePath);

        List<YearlyAverage> result = service.findAverageCityTemperaturesWithStream("warszawa");

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(7);
    }

    @Test
    public void stream_shouldCalculateNoAverages_whenCityNotFound() {
        service.setInputFilePath(correctFilePath);

        List<YearlyAverage> result = service.findAverageCityTemperaturesWithStream("szczecin");

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void stream_throwException_whenFileNotFound() {
        service.setInputFilePath(wrongFilePath);

        Exception exception = Assertions.assertThrows(FileReadException.class,
                () -> service.findAverageCityTemperaturesWithStream("warszawa"));

        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains("Error reading file:"));
    }
}
