package ru.springproject.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.springproject.utils.UserNotFoundException;

import java.util.Locale;

@RequestMapping("api/v1")
@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionsHandlerController {

    private final MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> internalServerErrorHandle(Exception e, Locale locale) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
                messageSource.getMessage("errors.user.internal_server_error",
                        new Object[0], "errors.user.internal_server_error", locale));

        problemDetail.setProperty("error", e.getMessage());

        return ResponseEntity.internalServerError().body(problemDetail);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ProblemDetail> userNotFoundExceptionHandle(UserNotFoundException e, Locale locale) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
                messageSource.getMessage("errors.user.not_found",
                        new Object[0], "errors.user.not_found", locale));

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    @ExceptionHandler(BindException.class)
    ResponseEntity<ProblemDetail> bindExceptionHandle(BindException e, Locale locale) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, this.messageSource.
                getMessage("errors.user.bind_exception",
                        new Object[0], "errors.user.bind_exception", locale));

        problemDetail.setProperty("errors", e.getBindingResult().getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .toList());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }
}
