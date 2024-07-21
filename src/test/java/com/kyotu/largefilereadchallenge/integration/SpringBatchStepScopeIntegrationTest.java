package com.kyotu.largefilereadchallenge.integration;

import com.kyotu.largefilereadchallenge.configuration.batch.BatchConfig;
import com.kyotu.largefilereadchallenge.configuration.batch.MeasurementItemProcessor;
import com.kyotu.largefilereadchallenge.configuration.batch.MeasurementItemWriter;
import com.kyotu.largefilereadchallenge.model.data.Measurement;
import com.kyotu.largefilereadchallenge.model.data.calculation.Summary;
import com.kyotu.largefilereadchallenge.model.data.calculation.YearlyAverage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.StepScopeTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBatchTest
@EnableAutoConfiguration
@ContextConfiguration(classes = {BatchConfig.class})
public class SpringBatchStepScopeIntegrationTest {

    @Autowired
    private FlatFileItemReader<Measurement> itemReader;
    @Autowired
    private MeasurementItemProcessor itemProcessor;
    @Autowired
    private MeasurementItemWriter itemWriter;
    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;
    @Autowired
    private Summary summary;

    private JobParameters defaultJobParameters() {
        return new JobParametersBuilder()
                .addString("INPUT", "src/test/resources/temp-measurements-single.csv")
                .addString("CITY", "warszawa")
                .addLong("TIME", System.currentTimeMillis())
                .toJobParameters();
    }

    @AfterEach
    public void cleanUp() {
        jobRepositoryTestUtils.removeJobExecutions();
    }

    @Test
    public void shouldSucceed_whenReaderCalled() throws Exception {
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution(defaultJobParameters());

        StepScopeTestUtils.doInStepScope(stepExecution, () -> {
            Measurement measurement;
            itemReader.open(stepExecution.getExecutionContext());
            while ((measurement = itemReader.read()) != null) {

                assertThat(measurement.getCity()).isEqualTo("Warszawa");
                assertThat(measurement.getMeasuredAt()).isEqualTo("2018-01-01 00:00:00.000");
                assertThat(measurement.getTemperature()).isEqualTo(9.97);
            }
            itemReader.close();
            return null;
        });
    }

    @Test
    public void shouldSucceed_whenProcessorCalled() throws Exception {
        Measurement measurement = new Measurement("Warszawa", "2018-01-01 00:00:00.000", 9.97);
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution(defaultJobParameters());

        StepScopeTestUtils.doInStepScope(stepExecution, () -> {
            Measurement processedMeasurement = itemProcessor.process(measurement);

            assertThat(processedMeasurement).isNotNull();
            assertThat(processedMeasurement.getMeasuredAt()).isEqualTo("2018");

            return null;
        });
    }

    @Test
    public void shouldSucceed_whenWriterCalled() throws Exception {
        Measurement measurement = new Measurement("Warszawa", "2018-01-01 00:00:00.000", 9.97);
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution(defaultJobParameters());

        StepScopeTestUtils.doInStepScope(stepExecution, () -> {
            itemWriter.write(new Chunk<>(List.of(measurement)));
            return null;
        });

        List<YearlyAverage> actualResponse = summary.getResponse();

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.size()).isEqualTo(1);
        assertThat(actualResponse.get(0).getYear()).isEqualTo("2018-01-01 00:00:00.000");
        assertThat(actualResponse.get(0).getAverageTemperature()).isEqualTo(9.97);
    }
}