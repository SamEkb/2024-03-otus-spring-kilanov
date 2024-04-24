package ru.skilanov.spring.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skilanov.spring.dao.QuestionDao;
import ru.skilanov.spring.domain.Answer;
import ru.skilanov.spring.domain.Question;
import ru.skilanov.spring.domain.Student;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestServiceImplTest {
    public static final String FIRST_NAME = "Jimmy";
    public static final String LAST_NAME = "Page";
    @Mock
    private IOService ioService;

    @Mock
    private QuestionDao questionDao;

    @InjectMocks
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

        when(ioService.readIntForRange(anyInt(), anyInt(), anyString()))
                .thenReturn(1)
                .thenReturn(1);

        var result = testService.executeTestFor(student);

        verify(ioService, times(3)).printFormattedLine(anyString());
        verify(ioService, times(2)).readIntForRange(anyInt(), anyInt(), anyString());
        verify(questionDao, times(1)).findAll();

        assertEquals(2, result.getRightAnswersCount());
        assertEquals(fullName, result.getStudent().getFullName());
        Assertions.assertThat(result.getAnsweredQuestions())
                .containsExactlyInAnyOrderElementsOf(questions);
    }
}
