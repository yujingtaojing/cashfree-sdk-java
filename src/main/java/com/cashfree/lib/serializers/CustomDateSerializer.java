package com.cashfree.lib.serializers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import com.cashfree.lib.constants.Constants;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CustomDateSerializer extends JsonSerializer<LocalDateTime> {
  private final DateTimeFormatter dateTimeFormatter;

  private CustomDateSerializer(final String format) {
    this.dateTimeFormatter = new DateTimeFormatterBuilder()
        .appendPattern(format)
        .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
        .parseDefaulting(ChronoField.MINUTE_OF_DAY, 0)
        .parseDefaulting(ChronoField.SECOND_OF_DAY, 0)
        .toFormatter();
  }

  public CustomDateSerializer() {
    this.dateTimeFormatter = Constants.dateTimeFormatter;
  }

  @Override
  public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    if (value == null) {
      gen.writeNull();
    } else {
      gen.writeString(dateTimeFormatter.format(value));
    }
  }
}