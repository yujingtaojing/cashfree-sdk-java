package com.cashfree.lib.domains;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.experimental.Accessors;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@Accessors(chain = true)
public class TransferDetails {
  private String referenceId;

  private String bankAccount;

  private String beneId;

  private BigDecimal amount;

  private String status;

  private String utr;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime addedOn;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime processedOn;

  private Integer acknowledged;
}
