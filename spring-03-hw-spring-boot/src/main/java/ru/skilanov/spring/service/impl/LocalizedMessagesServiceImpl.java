package ru.skilanov.spring.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.skilanov.spring.config.LocaleProvider;
import ru.skilanov.spring.service.api.LocalizedMessagesService;

@RequiredArgsConstructor
@Service
public class LocalizedMessagesServiceImpl implements LocalizedMessagesService {

    private final LocaleProvider localeProvider;

    private final MessageSource messageSource;

    @Override
    public String getMessage(String code, Object... args) {
        return messageSource.getMessage(code, args, localeProvider.getLocale());
    }
}
