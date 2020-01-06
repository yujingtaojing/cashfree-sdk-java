package com.cashfree.lib.domains.request;

import java.math.BigDecimal;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BatchTransferRequest {
  private String batchTransferId;

  private String batchFormat;

  private Integer deleteBene;

  private String beneId;

  private BigDecimal amount;

  private String transferId;

  private String remarks;

  private String name;

  private String email;

  private String phone;

  private String bankAccount;

  private String ifsc;
}
