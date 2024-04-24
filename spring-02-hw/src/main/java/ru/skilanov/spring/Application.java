package ru.skilanov.spring;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.skilanov.spring.service.TestRunnerService;

@ComponentScan
@PropertySource(value = "classpath:application.properties")
@Configuration
public class Application {

    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(Application.class);
        TestRunnerService testRunnerService = context.getBean(TestRunnerService.class);
        testRunnerService.run();
    }
}
