package ru.skilanov.spring.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skilanov.spring.config.TestConfig;
import ru.skilanov.spring.domain.TestResults;
import ru.skilanov.spring.service.api.LocalizedIOService;
import ru.skilanov.spring.service.api.ResultService;

@Service
@RequiredArgsConstructor
public class ResultServiceImpl implements ResultService {

    private final TestConfig testConfig;

    private final LocalizedIOService localizedIOService;

    @Override
    public void showResult(TestResults testResult) {
        localizedIOService.printLine("");
        localizedIOService.printLineLocalized("ResultService.test.results");
        localizedIOService.printFormattedLineLocalized("ResultService.student",
                testResult.getStudent().getFullName());
        localizedIOService.printFormattedLineLocalized("ResultService.answered.questions.count",
                testResult.getAnsweredQuestions().size());
        localizedIOService.printFormattedLineLocalized("ResultService.right.answers.count",
                testResult.getRightAnswersCount());

        if (testResult.getRightAnswersCount() >= testConfig.getRightAnswersCountToPass()) {
            localizedIOService.printLineLocalized("ResultService.passed.test");
            return;
        }
        localizedIOService.printLineLocalized("ResultService.fail.test");
    }
}
