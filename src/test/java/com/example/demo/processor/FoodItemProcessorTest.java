package com.example.demo.processor;

import com.example.demo.entity.Food;
import com.example.demo.entity.FoodDate;

import org.junit.jupiter.api.Test;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.test.StepScopeTestExecutionListener;

import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestExecutionListeners;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBatchTest
@SpringBootTest
@TestExecutionListeners({ StepScopeTestExecutionListener.class })
public class FoodItemProcessorTest {

    @MockBean
    private ItemProcessor<Food, FoodDate> itemProcessor = new FoodItemProcessor();

    @Test
    public void testFoodItemProcessor() throws Exception {
        Food food = new Food("FoodName", 2, 10, "FoodCategory");


        ExecutionContext executionContext = new ExecutionContext();
        executionContext.put("food", food);


        FoodDate transformedFood = itemProcessor.process(food);


        assert transformedFood != null;
        assertEquals("FoodName", transformedFood.getName());
        assertEquals(Integer.valueOf(20), transformedFood.getQuantity()); // 10 + 10
        assertEquals("FoodCategory", transformedFood.getCategory());
        assertEquals(2, transformedFood.getPrice());

    }
}

