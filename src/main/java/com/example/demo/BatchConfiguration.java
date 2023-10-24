package com.example.demo;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class BatchConfiguration {


    @Bean
    public JpaCursorItemReader<Food> jpaCursorItemReader() {
        JpaCursorItemReader<Food> reader = new JpaCursorItemReader<>();
        reader.setQueryString("SELECT p FROM food p");
        reader.setSaveState(false);

        return reader;
    }

    @Bean
    public FoodItemProcessor processor() {
        return new FoodItemProcessor();
    }

    @Bean
    public FlatFileItemWriter itemWriter() {
        return  new FlatFileItemWriterBuilder<Food>()
                .name("itemWriter")
                .resource(new FileSystemResource("target/test-outputs/output.txt"))
                .lineAggregator(new PassThroughLineAggregator<>())
                .build();
    }
}
