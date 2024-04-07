package ru.skilanov.spring;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.skilanov.spring.service.TestRunnerService;

public class Application {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");
        TestRunnerService testRunnerService = context.getBean(TestRunnerService.class);
        testRunnerService.run();
    }
}
