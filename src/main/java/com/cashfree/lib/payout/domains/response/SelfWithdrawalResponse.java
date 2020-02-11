package com.cashfree.lib.payout.domains.response;

import lombok.Data;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString(callSuper=true)
@EqualsAndHashCode(callSuper = true)
public class SelfWithdrawalResponse extends CfPayoutsResponse {
  private Integer statusCode;
}
