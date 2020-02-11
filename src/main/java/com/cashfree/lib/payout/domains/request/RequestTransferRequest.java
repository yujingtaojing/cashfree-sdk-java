package com.cashfree.lib.payout.domains.request;

import java.math.BigDecimal;

import lombok.Data;
import lombok.experimental.Accessors;

import com.cashfree.lib.annotations.NotNull;

@Data
@Accessors(chain = true)
public class RequestTransferRequest {
  @NotNull
  private String beneId;

  @NotNull
  private BigDecimal amount;

  @NotNull
  private String transferId;

  private String transferMode;

  private String remarks;
}
