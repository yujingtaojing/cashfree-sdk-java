package com.cashfree.lib.pg.domains.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GetOrderLinkResponse {
  private String status;

  private String paymentLink;

  private String reason;
}
