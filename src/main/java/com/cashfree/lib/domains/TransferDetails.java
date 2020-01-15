package com.cashfree.lib.domains;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.experimental.Accessors;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import com.cashfree.lib.serializers.CustomDateSerializer;
import com.cashfree.lib.serializers.CustomDateDeserializer;

@Data
@Accessors(chain = true)
public class TransferDetails {
  private String referenceId;

  private String bankAccount;

  private String beneId;

  private BigDecimal amount;

  private String status;

  private String utr;

  @JsonSerialize(using = CustomDateSerializer.class)
  @JsonDeserialize(using = CustomDateDeserializer.class)
  private LocalDateTime addedOn;

  @JsonSerialize(using = CustomDateSerializer.class)
  @JsonDeserialize(using = CustomDateDeserializer.class)
  private LocalDateTime processedOn;

  private Integer acknowledged;
}
