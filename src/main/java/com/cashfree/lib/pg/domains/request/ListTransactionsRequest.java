package com.cashfree.lib.pg.domains.request;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import lombok.Data;
import lombok.experimental.Accessors;

import com.cashfree.lib.annotations.NotNull;

@Data
@Accessors(chain = true)
public class ListTransactionsRequest {
  @NotNull
  private String startDate;

  @NotNull
  private String endDate;

  private String txStatus;

  private String lastId;

  private Integer count;

  public ListTransactionsRequest setStartDate(LocalDateTime startDate) {
    DateTimeFormatter dateTimeFormatter =
        new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd").toFormatter();
    this.startDate = dateTimeFormatter.format(startDate);
    return this;
  }

  private void setStartDate(String startDate) {}

  public ListTransactionsRequest setEndDate(LocalDateTime endDate) {
    DateTimeFormatter dateTimeFormatter =
        new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd").toFormatter();
    this.endDate = dateTimeFormatter.format(endDate);
    return this;
  }

  private void setEndDate(String endDate) {}
}
