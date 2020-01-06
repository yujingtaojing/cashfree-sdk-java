package com.cashfree.lib.domains.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BulkValidationRequest {
  private String bulkValidationId;

  private Payload[] entries;

  @Data
  @Accessors(chain = true)
  public final class Payload {
    private String name;

    private String phone;

    private String bankAccount;

    private String ifsc;
  }
}
