package com.kyotu.largefilereadchallenge.service;

import com.kyotu.largefilereadchallenge.model.data.calculation.Summary;
import com.kyotu.largefilereadchallenge.model.data.calculation.YearlyAverage;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BatchService {

    private final JobLauncher jobLauncher;
    private final Job job;
    private final Summary summary;

    @Value("${com.kyotu.file-path:/temp-measurements.csv}")
    private String inputFilePath;

    public List<YearlyAverage> findAverageCityTemperatures(String city) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("INPUT", inputFilePath)
                .addString("CITY", city.toLowerCase())
                .addLong("TIME", System.currentTimeMillis())
                .toJobParameters();
        jobLauncher.run(job, jobParameters);

        return summary.getResponse();
    }

}
