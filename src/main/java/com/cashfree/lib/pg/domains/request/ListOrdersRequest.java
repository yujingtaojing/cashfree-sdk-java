package com.cashfree.lib.pg.domains.request;

import lombok.Data;
import lombok.experimental.Accessors;

import com.cashfree.lib.annotations.NotNull;

@Data
@Accessors(chain = true)
public class ListOrdersRequest {
  @NotNull
  private String startDate;

  @NotNull
  private String endDate;

  private String txStatus;

  @NotNull
  private String lastId;

  @NotNull
  private Integer count;
}
