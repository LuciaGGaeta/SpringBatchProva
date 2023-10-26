package com.example.demo.writer;

import com.example.demo.entity.FoodDate;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.core.io.FileSystemResource;

public class FoodStep2Writer extends FlatFileItemWriter<FoodDate> {
    public FoodStep2Writer() {
        setName("itemWriter");
        setResource(new FileSystemResource("src/main/java/com/example/demo/step2.txt"));
        setLineAggregator(new PassThroughLineAggregator<>());
    }
}
