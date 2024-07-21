package com.kyotu.largefilereadchallenge.configuration.batch;

import com.kyotu.largefilereadchallenge.model.data.Measurement;
import com.kyotu.largefilereadchallenge.model.data.calculation.Summary;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MeasurementItemWriter implements ItemWriter<Measurement> {

    private final Summary summary;

    @Override
    public void write(Chunk<? extends Measurement> chunk) {
        chunk.forEach(summary::updateTotal);
    }
}