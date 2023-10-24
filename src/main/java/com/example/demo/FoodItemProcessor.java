package com.example.demo;

import com.example.demo.entity.Food;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import org.slf4j.Logger;

public class FoodItemProcessor implements ItemProcessor<Food,Food> {
    private static final Logger log = (Logger) LoggerFactory.getLogger(FoodItemProcessor.class);

    @Override
    public Food process(final Food food) {
        Integer quantity = food.getQuantity();
        final Integer quantityFinal = quantity++;



        final Food transformedFood = new Food(food.getName(), quantityFinal, food.getPrice(), food.getCategory());

        log.info("Converting (" + food + ") into (" + transformedFood + ")");

        return transformedFood;
    }
}
