package com.titikkoma.taska.base.helpers;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class DateFormatter {
    public static Timestamp formatDateToTimestamp(String date, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDate localDate = LocalDate.parse(date, formatter);
        long epochMillis = localDate
                .atStartOfDay(ZoneOffset.UTC)
                .toInstant()
                .toEpochMilli();
        return new Timestamp(epochMillis);
    }
}
