package com.kyotu.largefilereadchallenge.configuration.batch;

import com.kyotu.largefilereadchallenge.configuration.DynamicResourceLoader;
import com.kyotu.largefilereadchallenge.model.data.Measurement;
import com.kyotu.largefilereadchallenge.model.data.calculation.Summary;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    private final ResourceLoader resourceLoader;

    @Bean
    public Summary summary() {
        return new Summary();
    }

    @Bean
    @StepScope
    public DynamicResourceLoader resourceLoader() {
        return new DynamicResourceLoader(resourceLoader);
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Measurement> reader(@Value("#{jobParameters['INPUT']}") String input) {
        FlatFileItemReader<Measurement> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setName("measurementsReader");
        flatFileItemReader.setResource(resourceLoader().getResource(input));
        flatFileItemReader.setLineMapper(lineMapper());
        return flatFileItemReader;
    }

    @Bean
    public LineMapper<Measurement> lineMapper() {
        DefaultLineMapper<Measurement> defaultLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(";");
        lineTokenizer.setNames("city", "measuredAt", "temperature");
        lineTokenizer.setStrict(false);
        defaultLineMapper.setLineTokenizer(lineTokenizer);
        BeanWrapperFieldSetMapper<Measurement> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Measurement.class);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);
        return defaultLineMapper;
    }

    @Bean
    @StepScope
    public MeasurementItemProcessor itemProcessor() {
        return new MeasurementItemProcessor();
    }

    @Bean
    @StepScope
    public MeasurementItemWriter writer() {
        return new MeasurementItemWriter(summary());
    }

    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor("tempMeasurement");
    }

    @Bean
    public Job importMeasurementJob(JobRepository jobRepository, PlatformTransactionManager transactionManager, FlatFileItemReader<Measurement> reader) {
        Step importMeasurementStep = new StepBuilder("importMeasurementStep", jobRepository)
                .<Measurement, Measurement>chunk(1000, transactionManager)
                .reader(reader)
                .processor(itemProcessor())
                .writer(writer())
                .taskExecutor(taskExecutor())
                .build();


        return new JobBuilder("importMeasurementJob", jobRepository)
                .start(importMeasurementStep)
                .build();
    }
}
