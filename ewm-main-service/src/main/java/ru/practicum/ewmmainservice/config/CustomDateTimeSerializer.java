package ru.practicum.ewmmainservice.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ru.practicum.ewmmainservice.EwmMainServiceConstants.DATE_TIME_FORMAT;

public class CustomDateTimeSerializer extends StdSerializer<LocalDateTime> {

    protected CustomDateTimeSerializer() {
        this(null);
    }

    protected CustomDateTimeSerializer(Class<LocalDateTime> t) {
        super(t);
    }

    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        gen.writeString(formatter.format(value));
    }

}
