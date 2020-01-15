package com.cashfree.lib.domains.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

import com.cashfree.lib.domains.BeneficiaryDetails;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class GetBeneficiaryResponse extends CfPayoutsResponse {
  private Payload data;

  @Data
  @Accessors(chain = true)
  public static final class Payload {
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

    private String status;

    public BeneficiaryDetails getBeneficiaryDetails() {
      return new BeneficiaryDetails()
          .setBeneId(beneId)
          .setName(name)
          .setEmail(email)
          .setPhone(phone)
          .setBankAccount(bankAccount)
          .setIfsc(ifsc)
          .setAddress1(address1)
          .setCity(city)
          .setState(state)
          .setPincode(pincode);
    }
  }
}

