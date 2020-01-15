package com.cashfree.lib.domains.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DeactivateCashgramRequest {
  private String cashgramId;
}
