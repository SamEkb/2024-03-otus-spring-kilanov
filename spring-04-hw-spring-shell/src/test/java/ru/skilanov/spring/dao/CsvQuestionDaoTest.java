package ru.skilanov.spring.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.skilanov.spring.config.TestFileNameProvider;
import ru.skilanov.spring.domain.Question;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@SpringBootTest(classes = {CsvQuestionDao.class})
public class CsvQuestionDaoTest {

    @MockBean
    private TestFileNameProvider fileNameProviderMock;

    @Autowired
    private CsvQuestionDao dao;

    @Test
    void whenReadCsvThenReturnAllQuestions() {
        when(fileNameProviderMock.getTestFileName()).thenReturn("test_questions.csv");

        List<Question> questions = dao.findAll();

        assertEquals(5, questions.size());
        assertEquals("Is there life on Mars?", questions.get(0).text());
    }
}
