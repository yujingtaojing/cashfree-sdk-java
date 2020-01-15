package com.cashfree.lib.serializers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.DeserializationContext;

import com.cashfree.lib.constants.Constants;

public class CustomDateDeserializer extends JsonDeserializer<LocalDateTime> {
  private final DateTimeFormatter dateTimeFormatter;

  private CustomDateDeserializer(final String format) {
    this.dateTimeFormatter = new DateTimeFormatterBuilder()
        .appendPattern(format)
        .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
        .parseDefaulting(ChronoField.MINUTE_OF_DAY, 0)
        .parseDefaulting(ChronoField.SECOND_OF_DAY, 0)
        .toFormatter();
  }

  public CustomDateDeserializer() {
    this.dateTimeFormatter = Constants.dateTimeFormatter;
  }

  @Override
  public LocalDateTime deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
    String dateAsString = parser.getText();

    if (StringUtils.isBlank(dateAsString) || Constants.PLACEHOLDER_DATESTRING.equals(dateAsString)) {
      return null;
    }

    return LocalDateTime.parse(dateAsString, dateTimeFormatter);
  }
}