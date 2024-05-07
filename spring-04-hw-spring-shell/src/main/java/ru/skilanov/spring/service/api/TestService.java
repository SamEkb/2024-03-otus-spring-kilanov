package ru.skilanov.spring.service.api;

import ru.skilanov.spring.domain.Student;
import ru.skilanov.spring.domain.TestResults;

public interface TestService {

    TestResults executeTestFor(Student student);
}
