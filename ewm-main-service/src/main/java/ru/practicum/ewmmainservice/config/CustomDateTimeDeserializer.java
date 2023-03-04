package ru.practicum.ewmmainservice.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ru.practicum.ewmmainservice.EwmMainServiceConstants.DATE_TIME_FORMAT;

public class CustomDateTimeDeserializer extends StdDeserializer<LocalDateTime> {

    protected CustomDateTimeDeserializer() {
        this(null);
    }

    protected CustomDateTimeDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
        String value = jsonParser.getText();
        if (!value.equals("")) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
            return LocalDateTime.parse(value, formatter);
        }
        return null;
    }
}
