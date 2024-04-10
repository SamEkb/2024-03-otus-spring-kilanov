package ru.skilanov.spring.dao.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.skilanov.spring.domain.Answer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AnswerCsvConverterTest {

    private AnswerCsvConverter answerCsvConverter;

    @BeforeEach
    public void setUp() {
        answerCsvConverter = new AnswerCsvConverter();
    }

    @Test
    public void whenConvertAnswerThenItConverted() {
        Answer answer = (Answer) answerCsvConverter.convertToRead("It is right answer!%true");
        assertEquals("It is right answer!", answer.text());
        assertTrue(answer.isCorrect());
    }
}
