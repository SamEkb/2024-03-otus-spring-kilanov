package ru.skilanov.spring.dao;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.skilanov.spring.config.TestFileNameProvider;
import ru.skilanov.spring.dao.dto.QuestionDto;
import ru.skilanov.spring.domain.Question;
import ru.skilanov.spring.exception.QuestionReadException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        InputStream inputStream = getInputStream();
        return readQuestionsFromCsv(inputStream);
    }

    private List<Question> readQuestionsFromCsv(InputStream inputStream) throws QuestionReadException {
        List<Question> questions;

        try (InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(streamReader)) {

            CsvToBean<QuestionDto> csvToBean = buildCsvToBean(bufferedReader);
            List<QuestionDto> questionDtos = csvToBean.parse();

            if (questionDtos.isEmpty()) {
                throw new QuestionReadException("No questions were found!", new RuntimeException());
            }

            questions = questionDtos.stream()
                    .map(QuestionDto::toDomainObject)
                    .collect(Collectors.toList());

        } catch (IOException e) {
            throw new QuestionReadException("Error parsing CSV data", e);
        }

        return questions;
    }

    private CsvToBean<QuestionDto> buildCsvToBean(BufferedReader reader) {
        return new CsvToBeanBuilder<QuestionDto>(reader)
                .withSkipLines(1)
                .withSeparator(';')
                .withType(QuestionDto.class)
                .build();
    }

    private InputStream getInputStream() {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileNameProvider.getTestFileName());
        return Objects.requireNonNull(inputStream);
    }
}
