package com.cashfree.lib.pg.domains.response;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class Settlement {
  private String id;

  private BigDecimal totalTxAmount;

  private BigDecimal settlementAmount;

  private String adjustment;

  private BigDecimal amountSettled;

  private String transactionFrom;

  private String transactionTill;

  private String utr;

  private String settledOn;
}
