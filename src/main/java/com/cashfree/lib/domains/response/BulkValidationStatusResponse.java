package com.cashfree.lib.domains.response;

import java.util.Map;
import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class BulkValidationStatusResponse extends CfPayoutsResponse {
  private Payload data;

  @Data
  @Accessors(chain = true)
  public static final class Payload {
    private String bulkValidationId;

    private Map<String, Entry> entries;

    @Data
    @Accessors(chain = true)
    public static final class Entry {
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
}
