package com.cashfree.lib.pg.domains.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class VerifyCredentialsResponse {
  private String status;

  private String reason;
}
