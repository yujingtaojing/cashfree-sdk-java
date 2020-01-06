package com.cashfree.lib.domains.response;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class BatchTransferResponse extends CfPayoutsResponse {
  private List<RequestTransferResponse> batch;
}
