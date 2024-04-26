package ru.skilanov.spring.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Locale;
import java.util.Map;

@ConfigurationProperties(prefix = "application")
public class AppProperties implements TestFileNameProvider, TestConfig, LocaleProvider {
    @Getter
    @Setter
    private int rightAnswersCountToPass;

    @Getter
    private Locale locale;

    @Getter
    @Setter
    private Map<String, String> fileNameByLocaleTag;

    public void setLocale(String locale) {
        this.locale = Locale.forLanguageTag(locale);
    }

    @Override
    public String getTestFileName() {
        return fileNameByLocaleTag.get(locale.toLanguageTag());
    }
}
