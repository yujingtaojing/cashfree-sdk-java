package com.cashfree.lib.domains.response;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class BulkValidationStatusResponse extends CfPayoutsResponse {
  private String bulkValidationId;

  private Payload[] entries;

  @Data
  @Accessors(chain = true)
  public final class Payload {
    private String name;

    private String phone;

    private String bankAccount;

    private String ifsc;

    private String accountExists;

    private String nameAtBank;

    private BigDecimal amountDeposited;

    private String utr;

    private String refId;

    private String message;
  }
}
