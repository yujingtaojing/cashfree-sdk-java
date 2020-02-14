package com.cashfree.lib.pg.domains.request;

import java.math.BigDecimal;

import lombok.Data;
import lombok.experimental.Accessors;

import com.cashfree.lib.annotations.NotNull;

@Data
@Accessors(chain = true)
public class CreateOrderRequest {
  @NotNull
  private String orderId;

  @NotNull
  private BigDecimal orderAmount;

  private String orderCurrency;

  private String orderNote;

  @NotNull
  private String customerName;

  @NotNull
  private String customerPhone;

  @NotNull
  private String customerEmail;

  private String sellerPhone;

  @NotNull
  private String returnUrl;

  private String notifyUrl;

  private String pc;
}
