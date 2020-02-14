package com.cashfree.lib.pg.domains.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TriggerPaymentEmailResponse {
  private String status;

  private String message;

  private String reason;
}
