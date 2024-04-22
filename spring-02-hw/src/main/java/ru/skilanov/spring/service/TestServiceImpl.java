package ru.skilanov.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skilanov.spring.dao.QuestionDao;
import ru.skilanov.spring.domain.Answer;
import ru.skilanov.spring.domain.Question;
import ru.skilanov.spring.domain.Student;
import ru.skilanov.spring.domain.TestResults;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResults executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var questions = questionDao.findAll();
        var testResult = new TestResults(student);

        for (var question : questions) {
            var isAnswerValid = false;
            var answerCounter = 0;
            ioService.printFormattedLine(question.text());
            printAnswers(question, answerCounter);

            var studentAnswer = ioService.readStringWithPrompt("Please choose correct answer:");

            isAnswerValid = checkAnswer(question, studentAnswer);

            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

    private static Boolean checkAnswer(Question question, String studentAnswer) {
        try {
            int answerIndex = Integer.parseInt(studentAnswer) - 1;
            return isValidIndex(question, answerIndex) && question.answers().get(answerIndex).isCorrect();

        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isValidIndex(Question question, int answerIndex) {
        return answerIndex >= 0 && answerIndex < question.answers().size();
    }

    private void printAnswers(Question question, int answerCounter) {
        for (Answer answer : question.answers()) {
            answerCounter++;
            ioService.printLine(answerCounter + "." + " " + answer.text());
        }
    }
}
