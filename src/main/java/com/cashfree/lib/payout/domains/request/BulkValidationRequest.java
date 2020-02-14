package com.cashfree.lib.payout.domains.request;

import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

import com.cashfree.lib.annotations.NotNull;

@Data
@Accessors(chain = true)
public class BulkValidationRequest {
  private String bulkValidationId;

  private List<Payload> entries;

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
