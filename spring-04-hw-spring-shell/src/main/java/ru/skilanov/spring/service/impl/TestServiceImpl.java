package ru.skilanov.spring.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skilanov.spring.dao.QuestionDao;
import ru.skilanov.spring.domain.Answer;
import ru.skilanov.spring.domain.Question;
import ru.skilanov.spring.domain.Student;
import ru.skilanov.spring.domain.TestResults;
import ru.skilanov.spring.service.api.LocalizedIOService;
import ru.skilanov.spring.service.api.TestService;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final LocalizedIOService localizedIOService;

    private final QuestionDao questionDao;

    @Override
    public TestResults executeTestFor(Student student) {
        localizedIOService.printLine("");
        localizedIOService.printFormattedLineLocalized("TestService.answer.the.questions");
        var questions = questionDao.findAll();
        var testResult = new TestResults(student);

        for (var question : questions) {
            var isAnswerValid = false;
            var answerCounter = 0;
            localizedIOService.printFormattedLine(question.text());
            printAnswers(question, answerCounter);

            var studentAnswer = localizedIOService
                    .readIntForRangeLocalized(1, questions.size(), "TestService.choose.answer");

            isAnswerValid = checkAnswer(question, studentAnswer);

            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

    private static Boolean checkAnswer(Question question, int studentAnswer) {
        int answerIndex = studentAnswer - 1;
        return question.answers().get(answerIndex).isCorrect();
    }

    private void printAnswers(Question question, int answerCounter) {
        for (Answer answer : question.answers()) {
            answerCounter++;
            localizedIOService.printLine(answerCounter + "." + " " + answer.text());
        }
    }
}
