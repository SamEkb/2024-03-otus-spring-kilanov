package ru.skilanov.spring.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skilanov.spring.config.TestConfig;
import ru.skilanov.spring.domain.Student;
import ru.skilanov.spring.domain.TestResults;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ResultServiceImplTest {
    public static final String FIRST_NAME = "Sam";
    public static final String LAST_NAME = "Kilanov";
    @InjectMocks
    private ResultServiceImpl resultService;

    @Mock
    private IOService ioService;

    @Mock
    private TestConfig testConfig;

    @Test
    public void whenShowResultsThenTheyShown() {
        when(testConfig.getRightAnswersCountToPass()).thenReturn(3);
        var student = new Student(FIRST_NAME, LAST_NAME);
        var testResults = new TestResults(student);

        testResults.setRightAnswersCount(3);

        resultService.showResult(testResults);

        verify(ioService, times(3)).printLine(anyString());
        verify(ioService, times(1))
                .printFormattedLine("Student: %s", testResults.getStudent().getFullName());
        verify(ioService, times(1))
                .printFormattedLine("Answered questions count: %d", testResults.getAnsweredQuestions().size());
        verify(ioService, times(1))
                .printFormattedLine("Right answers count: %d", testResults.getRightAnswersCount());
        verify(testConfig, times(1)).getRightAnswersCountToPass();
    }
}
