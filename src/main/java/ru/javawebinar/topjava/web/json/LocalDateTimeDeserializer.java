package ru.javawebinar.topjava.web.json;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    private final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctx) throws IOException, JacksonException {
        String str = p.getText();
        try {
            return LocalDateTime.parse(str, format);
        } catch (DateTimeParseException e) {
            System.err.println(e);
            return null;
        }
    }
}