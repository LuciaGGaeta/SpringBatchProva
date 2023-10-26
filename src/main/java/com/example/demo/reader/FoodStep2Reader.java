package com.example.demo.reader;

import com.example.demo.entity.FoodDate;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.item.database.JpaCursorItemReader;

public class FoodStep2Reader extends JpaCursorItemReader<FoodDate> {
    public FoodStep2Reader(EntityManagerFactory entityManagerFactory) {
        setEntityManagerFactory(entityManagerFactory);
        setQueryString("SELECT t FROM FoodDate t");
        setSaveState(true);
    }
}
