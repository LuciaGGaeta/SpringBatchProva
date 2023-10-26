package com.example.demo.reader;

import com.example.demo.entity.Food;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.item.database.JpaCursorItemReader;

public class FoodItemReader extends JpaCursorItemReader<Food> {
    public FoodItemReader(EntityManagerFactory entityManagerFactory) {
        setEntityManagerFactory(entityManagerFactory);
        setQueryString("SELECT t FROM Food t");
        setSaveState(true);
    }
}
