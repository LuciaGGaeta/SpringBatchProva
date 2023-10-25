package com.example.demo;

import com.example.demo.entity.Food;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.item.ItemProcessor;

import org.slf4j.Logger;

public class FoodItemProcessor implements ItemProcessor<Food,Food> {
    private static final Logger log = LoggerFactory.getLogger(FoodItemProcessor.class);


    @Override
    public Food process(final Food food) {

        final Integer quantityFinal = food.getQuantity() + 10;

        final Food transformedFood = new Food(food.getName(),food.getPrice(),  quantityFinal, food.getCategory());

        log.info("Converting (" + food + ") into (" + transformedFood + ")");

        return transformedFood;
    }
}
