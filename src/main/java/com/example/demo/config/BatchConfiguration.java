package com.example.demo.config;

import com.example.demo.entity.FoodDate;
import com.example.demo.processor.FoodItemProcessor;
import com.example.demo.processor.FoodStep2Processor;
import com.example.demo.reader.FoodStep2Reader;
import com.example.demo.reader.FoodItemReader;
import com.example.demo.utils.JobCompletionNotificationListener;
import com.example.demo.entity.Food;
import com.example.demo.writer.FoodStep2Writer;
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
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JpaCursorItemReader;

import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class BatchConfiguration {
    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);
    //Start step1
    @Bean
    @StepScope
    public  FoodItemReader reader(EntityManagerFactory entityManagerFactory){
        return new FoodItemReader(entityManagerFactory);
    }

    @Bean
    public FoodItemProcessor processor() {
        return new FoodItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<FoodDate> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<FoodDate>()
                .sql("INSERT INTO food_date (name, quantity, category, price, prod_date) VALUES (:name, :quantity, :category, :price, :prod_date)")
                .dataSource(dataSource)
                .beanMapped()
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, DataSourceTransactionManager transactionManager,
                      JpaCursorItemReader<Food> reader, FoodItemProcessor processor, JdbcBatchItemWriter<FoodDate> writer) {
        return new StepBuilder("step1", jobRepository)
                .<Food, FoodDate> chunk(3, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
    //End step1

    //Start step2
    @Bean
    @StepScope
    public FoodStep2Reader readerStep2(EntityManagerFactory entityManagerFactory){
        return new FoodStep2Reader(entityManagerFactory);
    }

    @Bean
    public FoodStep2Processor processorStep2() {
        return new FoodStep2Processor();
    }

    @Bean
    public FoodStep2Writer writerStep2(){
        return new FoodStep2Writer();
    }
    @Bean
    public Step step2(JobRepository jobRepository, DataSourceTransactionManager transactionManager,
                      JpaCursorItemReader<FoodDate> readerStep2, FoodStep2Processor processorStep2, FoodStep2Writer writerStep2) {
        return new StepBuilder("step2", jobRepository)
                .<FoodDate, FoodDate> chunk(3, transactionManager)
                .reader(readerStep2)
                .processor(processorStep2)
                .writer(writerStep2)
                .build();
    }
    //End step2

    @Bean
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
    @Bean
    public Job importUserJob(JobRepository jobRepository, Step step1, Step step2) {
        Job job = new JobBuilder("importUserJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step1)
                .next(step2)
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
}
