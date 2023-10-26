package com.example.demo.processor;

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
public class FoodStep2ProcessorTest {
    private FoodStep2Processor processor = new FoodStep2Processor();

    @Test
    public void testFoodItemProcessor() throws Exception {
        FoodDate foodDate = new FoodDate("FoodName", 10, "FoodCategory", 2,"26/10/2023");
        FoodDate transformedFood = processor.process(foodDate);


        assert transformedFood != null;
        assertEquals("FoodName", transformedFood.getName());
        assertEquals(Integer.valueOf(10), transformedFood.getQuantity());
        assertEquals("FoodCategory", transformedFood.getCategory());
        assertEquals(2, transformedFood.getPrice());
        assertEquals("26/10/2023", transformedFood.getProd_date());

    }
}


