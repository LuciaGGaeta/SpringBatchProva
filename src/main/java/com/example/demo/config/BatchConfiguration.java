package com.example.demo.config;

import com.example.demo.utils.FoodItemProcessor;
import com.example.demo.utils.JobCompletionNotificationListener;
import com.example.demo.entity.Food;
import jakarta.persistence.EntityManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class BatchConfiguration {
    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    @Bean
    @StepScope
    public JpaCursorItemReader<Food> jpaCursorItemReader(EntityManagerFactory entityManagerFactory) {
        JpaCursorItemReader<Food> reader = new JpaCursorItemReader<>();
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString("SELECT t FROM Food t");
        reader.setSaveState(true);
        return reader;
    }
    @Bean
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public FoodItemProcessor processor() {
        return new FoodItemProcessor();
    }

    @Bean
    public FlatFileItemWriter<Food> itemWriter() {
        return new FlatFileItemWriterBuilder<Food>()
                .name("itemWriter")
                .resource(new FileSystemResource("src/main/java/com/example/demo/prova.txt"))
                .lineAggregator(new PassThroughLineAggregator<>())
                .build();
    }

    @Bean
    public Job importUserJob(JobRepository jobRepository, Step step1) {
        Job job = new JobBuilder("importUserJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step1)
                .build();

        try {
            JobExecution jobExecution = jobRepository.getLastJobExecution(job.getName(), new JobParameters());
            if (jobExecution != null) {
                if (jobExecution.isRunning()) {
                    log.info("Il job è in esecuzione.");
                } else {
                    log.info("Il job non è in esecuzione.");
                }
            } else {
                log.info("Nessuna esecuzione precedente del job.");
            }
        } catch (Exception e) {
            log.error("Errore durante il recupero delle informazioni sullo stato del job.", e);
        }

        return job;
    }

    @Bean
    public Step step1(JobRepository jobRepository, DataSourceTransactionManager transactionManager,
                      JpaCursorItemReader<Food> reader, FoodItemProcessor processor, FlatFileItemWriter<Food> writer) {
        return new StepBuilder("step1", jobRepository)
                .<Food, Food> chunk(3, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}
