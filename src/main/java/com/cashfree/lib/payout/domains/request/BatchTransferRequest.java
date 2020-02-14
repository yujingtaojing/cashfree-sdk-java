package com.cashfree.lib.payout.domains.request;

import java.util.List;
import java.math.BigDecimal;

import lombok.Data;
import lombok.experimental.Accessors;

import com.cashfree.lib.annotations.NotNull;

@Data
@Accessors(chain = true)
public class BatchTransferRequest {
  @NotNull
  private String batchTransferId;

  @NotNull
  private String batchFormat;

  @NotNull
  private Integer deleteBene;

  private List<Payload> batch;

  @Data
  @Accessors(chain = true)
  public static final class Payload {
    private String transferId;

    private BigDecimal amount;

    private String phone;

    private String bankAccount;

    private String ifsc;

    private String email;

    private String name;

    private String remarks;
  }
}
