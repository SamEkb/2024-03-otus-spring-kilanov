package ru.skilanov.spring.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skilanov.spring.config.TestFileNameProvider;
import ru.skilanov.spring.domain.Question;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class CsvQuestionDaoTest {

    @Mock
    private TestFileNameProvider fileNameProviderMock;

    @Test
    void whenReadCsvThenReturnAllQuestions() {
        when(fileNameProviderMock.getTestFileName()).thenReturn("test_questions.csv");

        QuestionDao dao = new CsvQuestionDao(fileNameProviderMock);

        List<Question> questions = dao.findAll();

        assertEquals(5, questions.size());
        assertEquals("Is there life on Mars?", questions.get(0).text());
    }
}
