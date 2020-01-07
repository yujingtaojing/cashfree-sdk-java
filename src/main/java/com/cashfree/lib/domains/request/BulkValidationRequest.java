package com.cashfree.lib.domains.request;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class BulkValidationRequest {
  private String bulkValidationId;

  private Payload[] entries;

  @Data
  @Accessors(chain = true)
  public static final class Payload {
    @NotNull
    private String name;

    @NotNull
    private String phone;

    @NotNull
    private String bankAccount;

    @NotNull
    private String ifsc;
  }
}
