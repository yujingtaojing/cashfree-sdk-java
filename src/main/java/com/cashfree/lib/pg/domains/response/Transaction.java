package com.cashfree.lib.pg.domains.response;

import java.math.BigDecimal;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Transaction {
  private String id;

  private String orderId;

  private BigDecimal orderAmount;

  private String orderNote;

  private String customerName;

  private String customerPhone;

  private String customerEmail;

  private String referenceId;

  private BigDecimal txAmount;

  private String txStatus;

  private String txTime;

  private String settlementStatus;

  private String refundStatus;
}
