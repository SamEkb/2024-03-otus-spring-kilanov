package ru.skilanov.spring.dao;

import ru.skilanov.spring.domain.Question;

import java.util.List;

public interface QuestionDao {

    List<Question> findAll();
}
