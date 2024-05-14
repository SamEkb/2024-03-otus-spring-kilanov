package ru.skilanov.spring.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.stereotype.Component;
import ru.skilanov.spring.domain.Student;
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

    private Student student;


    @ShellMethod(value = "Register", key = {"r", "reg"})
    public void register() {
        this.student = studentService.determineCurrentStudent();
    }

    @ShellMethod(value = "Start test", key = {"s", "start"})
    public void run() {
        if (student != null) {
            var testResult = testService.executeTestFor(student);
            resultService.showResult(testResult);
        } else {
            System.out.println("Student is not registered. Please register before taking the test.");
        }
    }
}
