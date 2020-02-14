package com.cashfree.lib.payout.domains.request;

import java.math.BigDecimal;

import lombok.Data;
import lombok.experimental.Accessors;

import com.cashfree.lib.annotations.NotNull;

@Data
@Accessors(chain = true)
public class SelfWithdrawalRequest {
  @NotNull
  private String withdrawalId;

  @NotNull
  private BigDecimal amount;

  private String remarks;
}
