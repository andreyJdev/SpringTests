package ru.springproject.services;


import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.lang.String;

@Service
public class DateTimeServiceImpl implements DateTimeService {

    public String getDateTimeNow(Locale locale) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy:MM:dd'##':HH:mm:ss:SSSSSSSS");
        return formatter.format(now);
    }
}
