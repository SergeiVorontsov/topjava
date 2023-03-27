package ru.javawebinar.topjava.util.formatter;

import org.jetbrains.annotations.NotNull;
import org.springframework.format.Formatter;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CustomDateFormatter implements Formatter<LocalDate> {
    private final DateTimeFormatter formatter;

    public CustomDateFormatter(String pattern) {
        formatter = DateTimeFormatter.ofPattern(pattern);
    }

    @Override
    public @NotNull String print(@NotNull LocalDate date, @NotNull Locale locale) {
        return date.toString();
    }


    public @Nullable LocalDate parse(@Nullable String text, @NotNull Locale locale) throws ParseException {
        return StringUtils.hasLength(text) || !StringUtils.hasText("null") ? LocalDate.parse(text, formatter) : null;
    }
}