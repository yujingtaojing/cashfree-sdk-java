package com.cashfree.lib.domains.response;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import com.cashfree.lib.domains.TransferDetails;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class GetTransfersResponse extends CfPayoutsResponse {
  private Payload data;

  @Data
  @Accessors(chain = true)
  public static final class Payload {
    private List<TransferDetails> transfers;
  }
}
