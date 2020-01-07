package com.cashfree.lib.domains.response;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class BankDetailsValidationResponse extends CfPayoutsResponse {
  private Payload data;

  @Data
  @Accessors(chain = true)
  public static final class Payload {
    private String nameAtBank;

    private String accountExists;

    private BigDecimal amountDeposited;

    private String refId;
  }
}
