package ru.springproject.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.MapBindingResult;
import ru.springproject.utils.UserNotFoundException;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExceptionsHandlerControllerTest {

    @Mock
    MessageSource messageSource;

    @InjectMocks
    ExceptionsHandlerController controller;

    @Test
    void internalServerErrorHandle() {
        // given
        var e = new Exception("error_code");
        var locale = new Locale("ru");
        when(messageSource.getMessage("errors.user.internal_server_error", new Object[0], "errors.user.internal_server_error", locale))
                .thenReturn("Внутренняя ошибка сервера");
        // when
        var result = this.controller.internalServerErrorHandle(e, locale);
        // then
        assertNotNull(result);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertInstanceOf(ProblemDetail.class, result.getBody());
        assertEquals("Внутренняя ошибка сервера", result.getBody().getDetail());
    }

    @Test
    void userNotFoundExceptionHandle_ResultsNotFound() {
        // given
        var e = new UserNotFoundException("error_code");
        var locale = new Locale("ru");
        when(messageSource.getMessage("errors.user.not_found", new Object[0], "errors.user.not_found", locale))
                .thenReturn("Пользователь с указанным id не найден");
        // when
        var result = this.controller.userNotFoundExceptionHandle(e, locale);
        // then
        assertNotNull(result);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertInstanceOf(ProblemDetail.class, result.getBody());
        assertEquals("Пользователь с указанным id не найден", result.getBody().getDetail());
    }

    @Test
    void bindExceptionHandle_ResultsBadRequest() {
        // given
        var bindingResult = new MapBindingResult(Map.of(), "request");
        bindingResult.addError(new FieldError("request", "title", "Все поля должны быть заполнены"));
        var e = new BindException(bindingResult);
        var locale = new Locale("ru");
        when(messageSource.getMessage("errors.user.bind_exception",
                new Object[0], "errors.user.bind_exception", locale))
                .thenReturn("Некорректные данные");
        // when
        var result = this.controller.bindExceptionHandle(e, locale);
        // then
        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertInstanceOf(ProblemDetail.class, result.getBody());
        assertEquals("Некорректные данные", result.getBody().getDetail());
        assertEquals(List.of(new FieldError("request", "title", "Все поля должны быть заполнены")),
                e.getFieldErrors());
    }
}