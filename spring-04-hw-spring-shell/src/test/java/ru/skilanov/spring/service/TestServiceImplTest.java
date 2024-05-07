package ru.skilanov.spring.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.skilanov.spring.dao.QuestionDao;
import ru.skilanov.spring.domain.Answer;
import ru.skilanov.spring.domain.Question;
import ru.skilanov.spring.domain.Student;
import ru.skilanov.spring.service.api.LocalizedIOService;
import ru.skilanov.spring.service.impl.TestServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {TestServiceImpl.class})
public class TestServiceImplTest {
    public static final String FIRST_NAME = "Jimmy";
    public static final String LAST_NAME = "Page";
    @MockBean
    private LocalizedIOService ioService;

    @MockBean
    private QuestionDao questionDao;

    @Autowired
    private TestServiceImpl testService;

    List<Question> questions;

    @BeforeEach
    public void setUp() {
        var speciousQuestionAnswers = List.of(new Answer("human", true), new Answer("animal", false));
        var speciousQuestion = new Question("Who are you?", speciousQuestionAnswers);
        var ageQuestionAnswers = List.of(new Answer("yes", true), new Answer("no", false));
        var ageQuestion = new Question("Are you older then 18?", ageQuestionAnswers);

        questions = new ArrayList<>();

        questions.add(speciousQuestion);
        questions.add(ageQuestion);
    }

    @Test
    public void whenExecuteTestForAStudentThenReturnResults() {
        Student student = new Student(FIRST_NAME, LAST_NAME);
        var fullName = String.format("%s %s", FIRST_NAME, LAST_NAME);
        when(questionDao.findAll()).thenReturn(questions);

        when(ioService.readIntForRangeLocalized(anyInt(), anyInt(), anyString()))
                .thenReturn(1)
                .thenReturn(1);

        var result = testService.executeTestFor(student);

        verify(ioService, times(1)).printFormattedLineLocalized("TestService.answer.the.questions");
        verify(ioService, times(2)).readIntForRangeLocalized(anyInt(), anyInt(), anyString());
        verify(questionDao, times(1)).findAll();

        assertEquals(2, result.getRightAnswersCount());
        assertEquals(fullName, result.getStudent().getFullName());
        Assertions.assertThat(result.getAnsweredQuestions())
                .containsExactlyInAnyOrderElementsOf(questions);
    }
}
