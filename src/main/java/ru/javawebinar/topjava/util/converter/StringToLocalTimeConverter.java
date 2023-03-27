package ru.javawebinar.topjava.util.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class StringToLocalTimeConverter implements Converter<String, LocalTime> {
    private final DateTimeFormatter formatter;

    public StringToLocalTimeConverter(String pattern) {
        formatter = DateTimeFormatter.ofPattern(pattern);
    }

    @Override
    public @Nullable LocalTime convert(@Nullable String text) {

        return StringUtils.hasLength(text)/*||!StringUtils.hasText("null")*/ ? LocalTime.parse(text, formatter) : null;
    }
}
