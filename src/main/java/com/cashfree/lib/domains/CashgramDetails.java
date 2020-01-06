package com.cashfree.lib.domains;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@Accessors(chain = true)
public class CashgramDetails {
  @NotNull
  private String cashgramId;

  @NotNull
  private BigDecimal amount;

  @NotNull
  private String name;

  private String email;

  @NotNull
  private String phone;

  @NotNull
  @JsonFormat(pattern = "yyyy/MM/dd")
  private LocalDateTime linkExpiry;

  private String remarks;

  private Integer notifyCustomer;
}
