package com.example.demo.processor;

import com.example.demo.entity.FoodDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class FoodStep2Processor implements ItemProcessor<FoodDate, FoodDate>{
    private static final Logger log = LoggerFactory.getLogger(FoodItemProcessor.class);
    @Override
    public FoodDate process(FoodDate foodDate) {
        FoodDate foodModify = new FoodDate(foodDate.getName(), foodDate.getQuantity(), foodDate.getCategory(), foodDate.getPrice(), foodDate.getProd_date());
        if(foodDate.getCategory().equalsIgnoreCase("frutta")){
            foodModify.setPrice(foodDate.getPrice() + 1);
            log.info("Converting (" + foodDate + ") in (" + foodModify + ")");
        }



        return foodModify;
    }
}
