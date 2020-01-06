package com.cashfree.lib.domains.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RemoveBeneficiaryRequest {
  private String beneId;
}
