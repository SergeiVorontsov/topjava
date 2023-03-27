package ru.javawebinar.topjava.util.formatter;

import org.jetbrains.annotations.NotNull;
import org.springframework.format.Formatter;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CustomTimeFormatter implements Formatter<LocalTime> {
    private final DateTimeFormatter formatter;

    public CustomTimeFormatter(String pattern) {
        formatter = DateTimeFormatter.ofPattern(pattern);
    }

    @Override
    public @NotNull String print(@NotNull LocalTime time, @NotNull Locale locale) {
        return time.toString();
    }


    public @Nullable LocalTime parse(@Nullable String text, @NotNull Locale locale) throws ParseException {
        return StringUtils.hasLength(text) || !StringUtils.hasText("null") ? LocalTime.parse(text, formatter) : null;
    }
}