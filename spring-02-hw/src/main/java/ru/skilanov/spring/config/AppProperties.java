package ru.skilanov.spring.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class AppProperties implements TestFileNameProvider, TestConfig {
    private final String testFileName;

    private final int rightAnswersCountToPass;

    public AppProperties(@Value("${file.resource.path}") String testFileName,
                         @Value("${questions.number}") int rightAnswersCountToPass
    ) {
        this.testFileName = testFileName;
        this.rightAnswersCountToPass = rightAnswersCountToPass;
    }
}
