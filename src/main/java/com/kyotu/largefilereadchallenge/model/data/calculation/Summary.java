package com.kyotu.largefilereadchallenge.model.data.calculation;

import com.kyotu.largefilereadchallenge.model.data.Measurement;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class Summary {
    private final ConcurrentMap<String, Total> yearlyTotals = new ConcurrentHashMap<>();

    public List<YearlyAverage> getResponse() {
        List<YearlyAverage> response = this.yearlyTotals.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> new YearlyAverage(e.getKey(), e.getValue().getAverage())).toList();
        yearlyTotals.clear();
        return response;
    }

    public void updateTotal(Measurement measurement) {
        yearlyTotals.merge(measurement.getMeasuredAt(), new Total(measurement.getTemperature()),
                (prev, value) -> prev.getUpdatedTotal(value.getSum()));
    }
}
