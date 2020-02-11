package com.cashfree.lib.serializers;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import com.cashfree.lib.utils.CommonUtils;
import com.cashfree.lib.constants.Constants;

public class CustomDateDeserializer implements JsonFieldDeserializer<LocalDateTime> {
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

  public LocalDateTime deserialize(String dateAsString) {
    if (CommonUtils.isBlank(dateAsString) || Constants.PLACEHOLDER_DATESTRING.equals(dateAsString)) {
      return null;
    }

    return LocalDateTime.parse(dateAsString, dateTimeFormatter);
  }
}