package com.cashfree.lib.pg.domains.response;

import java.math.BigDecimal;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public final class Refund {
  private String refundId;

  private String orderId;

  private String arn;

  private String referenceId;

  private BigDecimal txAmount;

  private BigDecimal refundAmount;

  private String refundNote;

  private String processed;

  private String initiatedOn;

  private String processedOn;
}
