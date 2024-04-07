package ru.skilanov.spring.config;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AppProperties implements TestFileNameProvider {
    private String testFileName;
}
