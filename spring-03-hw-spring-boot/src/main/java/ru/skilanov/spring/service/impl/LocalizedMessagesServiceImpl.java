package ru.skilanov.spring.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.skilanov.spring.config.LocaleConfig;
import ru.skilanov.spring.service.api.LocalizedMessagesService;

@RequiredArgsConstructor
@Service
public class LocalizedMessagesServiceImpl implements LocalizedMessagesService {

    private final LocaleConfig localeConfig;

    private final MessageSource messageSource;

    @Override
    public String getMessage(String code, Object... args) {
        return messageSource.getMessage(code, args, localeConfig.getLocale());
    }
}
