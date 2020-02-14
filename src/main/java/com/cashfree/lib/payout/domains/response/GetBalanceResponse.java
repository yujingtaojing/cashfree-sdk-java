package com.cashfree.lib.payout.domains.response;

import java.math.BigDecimal;

import lombok.Data;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString(callSuper=true)
@EqualsAndHashCode(callSuper = true)
public class GetBalanceResponse extends CfPayoutsResponse {
  private LedgerDetails data;

  @Data
  @Accessors(chain = true)
  public static final class LedgerDetails {
    private BigDecimal balance;

    private BigDecimal availableBalance;
  }
}
