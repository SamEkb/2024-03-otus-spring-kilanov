package ru.skilanov.spring.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import ru.skilanov.spring.service.api.ResultService;
import ru.skilanov.spring.service.api.StudentService;
import ru.skilanov.spring.service.api.TestService;

@Service
@RequiredArgsConstructor
public class TestRunnerServiceImpl implements CommandLineRunner {

    private final TestService testService;

    private final StudentService studentService;

    private final ResultService resultService;

    @Override
    public void run(String... args) {
        var student = studentService.determineCurrentStudent();
        var testResult = testService.executeTestFor(student);
        resultService.showResult(testResult);
    }
}
