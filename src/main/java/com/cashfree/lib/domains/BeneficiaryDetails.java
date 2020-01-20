package com.cashfree.lib.domains;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class BeneficiaryDetails {
  @NotNull
  private String beneId;

  @NotNull
  private String name;

  @NotNull
  private String email;

  @NotNull
  private String phone;

  private String bankAccount;

  private String ifsc;

  private String address1;

  private String city;

  private String state;

  private String pincode;

  /**
   * @param beneId Can be only string without special characters.
   * @return
   */
  public BeneficiaryDetails setBeneId(String beneId) {
    this.beneId = beneId;
    return this;
  }
}
