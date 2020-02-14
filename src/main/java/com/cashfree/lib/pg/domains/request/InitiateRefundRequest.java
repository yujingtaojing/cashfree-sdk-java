package com.cashfree.lib.pg.domains.request;

import java.math.BigDecimal;

import lombok.Data;
import lombok.experimental.Accessors;

import com.cashfree.lib.annotations.NotNull;

@Data
@Accessors(chain = true)
public class InitiateRefundRequest {
  @NotNull
  private String referenceId;

  @NotNull
  private BigDecimal refundAmount;

  @NotNull
  private String refundNote;

  private String refundType;

  private String merchantRefundId;

  private String mode;

  private String accountNo;

  private String ifsc;
}
