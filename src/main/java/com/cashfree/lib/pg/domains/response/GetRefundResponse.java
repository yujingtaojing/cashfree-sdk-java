package com.cashfree.lib.pg.domains.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GetRefundResponse {
  private String status;

  private String message;

  private Refund refund;
}
