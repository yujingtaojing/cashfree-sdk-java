package com.cashfree.lib.payout.domains.response;

import com.cashfree.lib.annotations.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString(callSuper=true)
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

    private String vpa;

    private String maskedCard;

    private String address1;

    private String city;

    private String state;

    private String pincode;

    private String status;

  }
}

