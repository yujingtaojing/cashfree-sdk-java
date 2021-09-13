package com.cashfree.lib.payout.domains.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CfPayoutsResponse {
  private String status;

  private Integer subCode;

  private String message;

  private String xRequestId;
}
