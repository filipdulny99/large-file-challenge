package com.kyotu.largefilereadchallenge.integration;

import com.kyotu.largefilereadchallenge.configuration.batch.BatchConfig;
import com.kyotu.largefilereadchallenge.model.data.calculation.Summary;
import com.kyotu.largefilereadchallenge.model.data.calculation.YearlyAverage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBatchTest
@EnableAutoConfiguration
@ContextConfiguration(classes = {BatchConfig.class})
public class SpringBatchIntegrationTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;
    @Autowired
    private Summary summary;

    @AfterEach
    public void cleanUp() {
        jobRepositoryTestUtils.removeJobExecutions();
    }

    private JobParameters defaultJobParameters() {
        return new JobParametersBuilder()
                .addString("INPUT", "src/test/resources/temp-measurements.csv")
                .addString("CITY", "warszawa")
                .addLong("TIME", System.currentTimeMillis())
                .toJobParameters();
    }

    @Test
    public void shouldCompleteJob_whenJobExecuted() throws Exception {
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(defaultJobParameters());
        JobInstance actualJobInstance = jobExecution.getJobInstance();
        ExitStatus actualJobExitStatus = jobExecution.getExitStatus();

        List<YearlyAverage> actualResponse = summary.getResponse();

        assertThat(actualJobInstance.getJobName()).isEqualTo("importMeasurementJob");
        assertThat(actualJobExitStatus.getExitCode()).isEqualTo("COMPLETED");
        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.size()).isEqualTo(7);
    }

}
