package ru.javawebinar.topjava.util.converter;

import org.springframework.format.Formatter;
import ru.javawebinar.topjava.util.TimeUtil;

import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Created by Sheremet on 23.07.2016.
 */
public class LocalTimeFormatter implements Formatter<LocalTime> {
    @Override
    public LocalTime parse(String text, Locale locale) throws ParseException {
        return TimeUtil.parseLocalTime(text);
    }

    @Override
    public String print(LocalTime object, Locale locale) {
        return object.format(DateTimeFormatter.ISO_LOCAL_TIME);
    }
}
