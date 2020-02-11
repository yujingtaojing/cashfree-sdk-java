package com.cashfree.lib.payout.domains.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DeactivateCashgramRequest {
  private String cashgramId;
}
