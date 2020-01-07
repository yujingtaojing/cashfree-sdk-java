package com.cashfree.lib.domains;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import lombok.Data;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import com.cashfree.lib.constants.Constants;

@Data
@Accessors(chain = true)
public class CashgramDetails {
  private static final DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
      .appendPattern("yyyy/MM/dd")
      .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
      .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
      .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
      .toFormatter();

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
  @JsonSerialize(using = DateSerializer.class)
  @JsonDeserialize(using = DateDeserializer.class)
  private LocalDateTime linkExpiry;

  private String remarks;

  private Integer notifyCustomer;

  public static final class DateSerializer extends JsonSerializer<LocalDateTime> {
    public DateSerializer() {}

    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
      if (value == null) {
        gen.writeNull();
      } else {
        gen.writeString(dateTimeFormatter.format(value));
      }
    }
  }

  public static final class DateDeserializer extends JsonDeserializer<LocalDateTime> {
    public DateDeserializer() {}

    @Override
    public LocalDateTime deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
      String dateAsString = parser.getText();

      if (StringUtils.isBlank(dateAsString) || Constants.PLACEHOLDER_DATESTRING.equals(dateAsString)) {
        return null;
      }

      return LocalDateTime.parse(dateAsString, dateTimeFormatter);
    }
  }
}
