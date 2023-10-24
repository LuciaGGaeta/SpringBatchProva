package com.example.demo;

import com.example.demo.entity.Food;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EnableTransactionManagement
public class BatchConfiguration {


    @Bean
    public JpaCursorItemReader<Food> jpaCursorItemReader(EntityManagerFactory entityManagerFactory) {
        JpaCursorItemReader<Food> reader = new JpaCursorItemReader<>();
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString("SELECT t FROM food t");
        reader.setSaveState(true);
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
