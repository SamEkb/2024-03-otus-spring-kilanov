package ru.skilanov.spring;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import ru.skilanov.spring.service.TestRunnerService;

@ComponentScan
public class Application {

    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(Application.class);
        TestRunnerService testRunnerService = context.getBean(TestRunnerService.class);
        testRunnerService.run();
    }
}
