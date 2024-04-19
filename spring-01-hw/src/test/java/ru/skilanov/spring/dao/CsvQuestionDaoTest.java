package ru.skilanov.spring.dao;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.skilanov.spring.config.TestFileNameProvider;
import ru.skilanov.spring.domain.Question;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class CsvQuestionDaoTest {

    @Test
    void whenReadCsvThenReturnAllQuestions() {
        var fileNameProviderMock = Mockito.mock(TestFileNameProvider.class);
        Mockito.when(fileNameProviderMock.getTestFileName()).thenReturn("test_questions.csv");

        QuestionDao dao = new CsvQuestionDao(fileNameProviderMock);

        List<Question> questions = dao.findAll();

        assertEquals(5, questions.size());
        assertEquals("Is there life on Mars?", questions.get(0).text());
    }
}
