package ru.springproject.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.springproject.services.DateTimeService;

import java.util.Locale;

@RestController
@RequestMapping("api/v2")
@RequiredArgsConstructor
public class Controller {

    private final DateTimeService dateTimeService;

    @GetMapping("datetime")
    public ResponseEntity<String> getDateTime() {
        Locale locale = Locale.getDefault();
        String dateTimeNow = dateTimeService.getDateTimeNow(locale);
        return ResponseEntity.status(HttpStatus.OK).body(dateTimeNow);
    }
}