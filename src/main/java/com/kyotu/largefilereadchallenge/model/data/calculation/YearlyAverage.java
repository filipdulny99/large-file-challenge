package com.kyotu.largefilereadchallenge.model.data.calculation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class YearlyAverage {
    private String year;
    private Double averageTemperature;
}
