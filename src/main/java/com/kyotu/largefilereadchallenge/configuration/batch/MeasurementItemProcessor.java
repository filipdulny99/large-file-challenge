package com.kyotu.largefilereadchallenge.configuration.batch;

import com.kyotu.largefilereadchallenge.model.data.Measurement;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class MeasurementItemProcessor implements ItemProcessor<Measurement, Measurement> {

    @Value("#{jobParameters['CITY']}")
    private String city;

    @Override
    public Measurement process(Measurement item) {
        if (!this.city.equals(item.getCity().toLowerCase())) {
            return null;
        }
        item.setMeasuredAt(item.getMeasuredAt().substring(0, 4));
        return item;
    }
}