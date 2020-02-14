package com.cashfree.lib.payout.domains;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.experimental.Accessors;

import com.cashfree.lib.annotations.Serialize;
import com.cashfree.lib.annotations.Deserialize;
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

  @Serialize(using = CustomDateSerializer.class)
  @Deserialize(using = CustomDateDeserializer.class)
  private LocalDateTime addedOn;

  @Serialize(using = CustomDateSerializer.class)
  @Deserialize(using = CustomDateDeserializer.class)
  private LocalDateTime processedOn;

  private Integer acknowledged;
}
