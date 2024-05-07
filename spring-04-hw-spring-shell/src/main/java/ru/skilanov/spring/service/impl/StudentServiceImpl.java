package ru.skilanov.spring.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skilanov.spring.domain.Student;
import ru.skilanov.spring.service.api.LocalizedIOService;
import ru.skilanov.spring.service.api.StudentService;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final LocalizedIOService localizedIOService;

    @Override
    public Student determineCurrentStudent() {
        var firstName = localizedIOService.readStringWithPromptLocalized("StudentService.input.first.name");
        var lastName = localizedIOService.readStringWithPromptLocalized("StudentService.input.last.name");
        return new Student(firstName, lastName);
    }
}
