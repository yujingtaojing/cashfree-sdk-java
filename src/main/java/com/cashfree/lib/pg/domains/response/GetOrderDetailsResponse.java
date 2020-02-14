package com.cashfree.lib.pg.domains.response;

import java.math.BigDecimal;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GetOrderDetailsResponse {
  private String status;

  private TransferDetails details;

  @Data
  @Accessors(chain = true)
  public static final class TransferDetails {
    private String orderId;

    private BigDecimal orderAmount;

    private String orderNote;

    private String customerName;

    private String customerPhone;

    private String orderStatus;

    private String addedOn;
  }
}
