package ru.javawebinar.topjava.util.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StringToLocalDateConverter implements Converter<String, LocalDate> {
    private final DateTimeFormatter formatter;

    public StringToLocalDateConverter(String pattern) {
        formatter = DateTimeFormatter.ofPattern(pattern);
    }

    @Override
    public @Nullable LocalDate convert(@Nullable String text) {

        return StringUtils.hasLength(text) || !StringUtils.hasText("null") ? LocalDate.parse(text, formatter) : null;
    }
}
