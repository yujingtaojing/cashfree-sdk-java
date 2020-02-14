package com.cashfree.lib.payout.domains;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import lombok.Data;
import lombok.experimental.Accessors;

import com.cashfree.lib.utils.CommonUtils;
import com.cashfree.lib.constants.Constants;
import com.cashfree.lib.annotations.NotNull;
import com.cashfree.lib.annotations.Serialize;
import com.cashfree.lib.annotations.Deserialize;
import com.cashfree.lib.serializers.JsonFieldSerializer;
import com.cashfree.lib.serializers.JsonFieldDeserializer;

@Data
@Accessors(chain = true)
public class CashgramDetails {
  @NotNull
  private String cashgramId;

  @NotNull
  private BigDecimal amount;

  @NotNull
  private String name;

  private String email;

  @NotNull
  private String phone;

  @NotNull
  @Serialize(using = DateSerializer.class)
  @Deserialize(using = DateDeserializer.class)
  private LocalDateTime linkExpiry;

  private String remarks;

  private Integer notifyCustomer;

  public static final class DateSerializer implements JsonFieldSerializer<LocalDateTime> {
    private static final DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
        .appendPattern("yyyy/MM/dd")
        .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
        .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
        .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
        .toFormatter();

    public DateSerializer() {}

    @Override
    public String serialize(LocalDateTime value) {
      if (value == null) {
        return null;
      } else {
        return dateTimeFormatter.format(value);
      }
    }
  }

  public static final class DateDeserializer implements JsonFieldDeserializer<LocalDateTime> {
    private static final DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
        .appendPattern("yyyy/MM/dd")
        .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
        .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
        .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
        .toFormatter();

    public DateDeserializer() {}

    @Override
    public LocalDateTime deserialize(String dateAsString) {
      if (CommonUtils.isBlank(dateAsString) || Constants.PLACEHOLDER_DATESTRING.equals(dateAsString)) {
        return null;
      }

      return LocalDateTime.parse(dateAsString, dateTimeFormatter);
    }
  }
}
