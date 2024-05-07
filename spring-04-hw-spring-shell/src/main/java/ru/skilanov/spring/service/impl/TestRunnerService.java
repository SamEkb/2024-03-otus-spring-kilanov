package ru.skilanov.spring.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.stereotype.Component;
import ru.skilanov.spring.service.api.ResultService;
import ru.skilanov.spring.service.api.StudentService;
import ru.skilanov.spring.service.api.TestService;

@Component
@RequiredArgsConstructor
@ShellComponent
public class TestRunnerService {

    private final TestService testService;

    private final StudentService studentService;

    private final ResultService resultService;


    @ShellMethod(value = "Run command", key = {"r", "run"})
    public void run() {
        var student = studentService.determineCurrentStudent();
        var testResult = testService.executeTestFor(student);
        resultService.showResult(testResult);
    }
}
