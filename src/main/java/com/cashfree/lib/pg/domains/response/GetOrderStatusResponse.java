package com.cashfree.lib.pg.domains.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GetOrderStatusResponse {
  private String status;

  private String paymentLink;

  private String reason;

  private String txStatus;

  private String txTime;

  private String txMsg;

  private String referenceId;

  private String paymentMode;

  private String orderCurrency;

  private PaymentDetails paymentDetails;

  @Data
  @Accessors(chain = true)
  public static final class PaymentDetails {
    private String paymentMode;

    private String bankName;

    private String cardNumber;

    private String cardCountry;

    private String cardScheme;
  }
}
