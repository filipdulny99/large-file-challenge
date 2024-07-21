package com.kyotu.largefilereadchallenge.controller;

import com.kyotu.largefilereadchallenge.model.data.calculation.YearlyAverage;
import com.kyotu.largefilereadchallenge.service.BatchService;
import com.kyotu.largefilereadchallenge.service.TemperatureService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/temperature")
public class TemperatureController {

    private final BatchService batchService;
    private final TemperatureService service;

    @GetMapping("/batch/{city}")
    public ResponseEntity<List<YearlyAverage>> getAverageCityTemperaturesWithBatchJob(
            @Valid @PathVariable @Size(min = 2, message = "City name must be at least 2 characters long") String city) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        return ResponseEntity.ok(batchService.findAverageCityTemperatures(city));
    }

    @GetMapping("/agg/{city}")
    public ResponseEntity<List<YearlyAverage>> getAverageCityTemperaturesWithAggregation(
            @Valid @PathVariable @Size(min = 2, message = "City name must be at least 2 characters long") String city) {
        return ResponseEntity.ok(service.findAverageCityTemperaturesWithAggregation(city.toLowerCase()));
    }

    @GetMapping("/funct/{city}")
    public ResponseEntity<List<YearlyAverage>> getAverageCityTemperaturesWithStream(
            @Valid @PathVariable @Size(min = 2, message = "City name must be at least 2 characters long") String city) {
        return ResponseEntity.ok(service.findAverageCityTemperaturesWithStream(city.toLowerCase()));
    }
}
