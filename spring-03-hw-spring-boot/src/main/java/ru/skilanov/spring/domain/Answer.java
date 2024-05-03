package ru.skilanov.spring.domain;

import com.opencsv.bean.CsvBindByName;

public record Answer(@CsvBindByName String text, @CsvBindByName boolean isCorrect) {
}
