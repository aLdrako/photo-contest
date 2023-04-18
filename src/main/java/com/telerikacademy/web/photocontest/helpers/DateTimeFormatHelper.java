package com.telerikacademy.web.photocontest.helpers;

import java.time.Duration;
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

    public static String getPhaseRemainingTime(LocalDateTime phase) {
        LocalDateTime now = LocalDateTime.now();
        Duration untilPhaseEnds = Duration.between(now, phase);
        if (untilPhaseEnds.isNegative()) {
            return "Closed";
        } else {
            long days = untilPhaseEnds.toDays();
            untilPhaseEnds = untilPhaseEnds.minusDays(days);
            long hours = untilPhaseEnds.toHours();
            untilPhaseEnds = untilPhaseEnds.minusHours(hours);
            long minutes = untilPhaseEnds.toMinutes();
            return String.format("%02d days, %02d hours, %02d minutes", days, hours, minutes);
        }
    }
}
