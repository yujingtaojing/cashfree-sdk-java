package com.cashfree.lib.domains.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class CashgramCreationResponse extends CfPayoutsResponse {
  private Payload data;

  @Data
  @Accessors(chain = true)
  public static final class Payload {
    private String referenceId;

    private String cashgramLink;
  }
}
