package com.cashfree.lib.serializers;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import com.cashfree.lib.constants.Constants;

public class CustomDateSerializer implements JsonFieldSerializer<LocalDateTime> {
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
  public String serialize(LocalDateTime value) {
    if (value == null) {
      return null;
    } else {
      return dateTimeFormatter.format(value);
    }
  }
}