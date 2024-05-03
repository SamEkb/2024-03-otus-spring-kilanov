package ru.skilanov.spring.service.api;

public interface LocalizedMessagesService {
    String getMessage(String code, Object ...args);
}
