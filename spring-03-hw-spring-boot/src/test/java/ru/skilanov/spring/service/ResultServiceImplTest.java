package ru.skilanov.spring.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skilanov.spring.config.TestConfig;
import ru.skilanov.spring.domain.Student;
import ru.skilanov.spring.domain.TestResults;
import ru.skilanov.spring.service.api.IOService;
import ru.skilanov.spring.service.api.LocalizedIOService;
import ru.skilanov.spring.service.impl.ResultServiceImpl;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ResultServiceImplTest {
    public static final String FIRST_NAME = "Sam";
    public static final String LAST_NAME = "Kilanov";
    @InjectMocks
    private ResultServiceImpl resultService;

    @Mock
    private LocalizedIOService ioService;

    @Mock
    private TestConfig testConfig;

    @Test
    public void whenShowResultsThenTheyShown() {
        when(testConfig.getRightAnswersCountToPass()).thenReturn(3);
        var student = new Student(FIRST_NAME, LAST_NAME);
        var testResults = new TestResults(student);

        testResults.setRightAnswersCount(3);

        resultService.showResult(testResults);

        verify(ioService, times(2)).printLineLocalized(anyString());
        verify(ioService, times(1))
                .printFormattedLineLocalized("ResultService.student", testResults.getStudent().getFullName());
        verify(ioService, times(1))
                .printFormattedLineLocalized("ResultService.answered.questions.count", testResults.getAnsweredQuestions().size());
        verify(ioService, times(1))
                .printFormattedLineLocalized("ResultService.right.answers.count", testResults.getRightAnswersCount());
        verify(testConfig, times(1)).getRightAnswersCountToPass();
    }
}
