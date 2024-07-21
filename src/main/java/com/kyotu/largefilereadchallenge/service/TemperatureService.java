package com.kyotu.largefilereadchallenge.service;

import ch.randelshofer.fastdoubleparser.JavaDoubleParser;
import com.kyotu.largefilereadchallenge.exception.FileReadException;
import com.kyotu.largefilereadchallenge.model.data.calculation.Total;
import com.kyotu.largefilereadchallenge.model.data.calculation.YearlyAverage;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TemperatureService {

    @Setter
    @Value("${com.kyotu.file-path:/temp-measurements.csv}")
    private String inputFilePath;

    public List<YearlyAverage> findAverageCityTemperaturesWithAggregation(String targetCity) {
        Map<String, Total> yearlyTotals = new HashMap<>();
        try (Stream<String> lines = Files.lines(Paths.get(inputFilePath))) {
            lines
                    .filter(line -> line.toLowerCase().startsWith(targetCity))
                    .forEach(line -> {
                        String[] values = line.split(";");
                        String year = values[1].substring(0, 4);
                        double temperature = JavaDoubleParser.parseDouble(values[2]);

                        yearlyTotals.merge(year, new Total(temperature),
                                (prev, value) -> prev.getUpdatedTotal(value.getSum()));
                    });
            return yearlyTotals.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .map(e -> new YearlyAverage(e.getKey(), e.getValue().getAverage())).toList();
        } catch (IOException e) {
            throw new FileReadException("Error reading file: ".concat(e.getMessage()));
        }
    }

    public List<YearlyAverage> findAverageCityTemperaturesWithStream(String targetCity) {
        try (Stream<String> lines = Files.lines(Paths.get(inputFilePath))) {
            Map<String, Double> yearlyAverages = lines
                    .filter(line -> line.toLowerCase().startsWith(targetCity))
                    .map(line -> line.split(";"))
                    .collect(Collectors.groupingBy(arr -> arr[1].substring(0, 4),
                            Collectors.averagingDouble(arr -> JavaDoubleParser.parseDouble(arr[2]))));

            return yearlyAverages.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .map(e -> new YearlyAverage(e.getKey(), e.getValue())).toList();
        } catch (IOException e) {
            throw new FileReadException("Error reading file: ".concat(e.getMessage()));
        }
    }
}
