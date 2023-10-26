package com.example.demo.processor;

import com.example.demo.entity.Food;
import com.example.demo.entity.FoodDate;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.slf4j.Logger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FoodItemProcessor implements ItemProcessor<Food,FoodDate> {
    private static final Logger log = LoggerFactory.getLogger(FoodItemProcessor.class);



    @Override
    public FoodDate process(final Food food) {

        SimpleDateFormat dtf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();

        Date dateObj = calendar.getTime();
        String formattedDate = dtf.format(dateObj);

        final Integer quantityFinal = food.getQuantity() + 10;

        final FoodDate transformedFood = new FoodDate(food.getName(), quantityFinal, food.getCategory(), food.getPrice(), formattedDate);

        log.info("Converting (" + food + ") into (" + transformedFood + ")");

        return transformedFood;
    }
}
