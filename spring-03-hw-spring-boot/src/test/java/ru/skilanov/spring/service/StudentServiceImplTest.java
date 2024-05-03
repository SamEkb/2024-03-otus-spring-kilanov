package ru.skilanov.spring.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skilanov.spring.service.api.IOService;
import ru.skilanov.spring.service.api.LocalizedIOService;
import ru.skilanov.spring.service.impl.StudentServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StudentServiceImplTest {

    public static final String FIRST_NAME = "Sam";
    public static final String LAST_NAME = "Kilanov";

    @Mock
    private LocalizedIOService ioService;

    @InjectMocks
    private StudentServiceImpl studentService;

    @Test
    public void whenCreateUserThenItCreated() {
        when(ioService.readStringWithPromptLocalized("StudentService.input.first.name")).thenReturn(FIRST_NAME);
        when(ioService.readStringWithPromptLocalized("StudentService.input.last.name")).thenReturn(LAST_NAME);

        var createdStudent = studentService.determineCurrentStudent();

        assertEquals(FIRST_NAME, createdStudent.firstName());
        assertEquals(LAST_NAME, createdStudent.lastName());
    }

}
