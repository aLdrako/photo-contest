package com.telerikacademy.web.photocontest.helpers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeFormatHelper {
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");

    public static String formatToString(LocalDateTime localDateTime) {
        return localDateTime.format(dateTimeFormatter);
    }

    public static LocalDateTime formatToLocalDateTime(String localDateTimeAsString) {
        return LocalDateTime.parse(localDateTimeAsString, dateTimeFormatter);
    }
}
