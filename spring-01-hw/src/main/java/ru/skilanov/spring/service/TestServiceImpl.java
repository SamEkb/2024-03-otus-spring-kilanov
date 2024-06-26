package ru.skilanov.spring.service;

import lombok.RequiredArgsConstructor;
import ru.skilanov.spring.dao.QuestionDao;
import ru.skilanov.spring.domain.Answer;
import ru.skilanov.spring.domain.Question;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        for (Question question : questionDao.findAll()) {
            var answerCounter = 0;
            ioService.printFormattedLine(question.text());

            for (Answer answer : question.answers()) {
                answerCounter++;
                ioService.printLine(answerCounter + "." + " " + answer.text());
            }

            ioService.printLine("");
        }
    }
}
