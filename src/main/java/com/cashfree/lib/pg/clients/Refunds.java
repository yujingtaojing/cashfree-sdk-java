package com.cashfree.lib.pg.clients;

import java.util.Map;
import java.util.HashMap;

import com.cashfree.lib.utils.CommonUtils;
import com.cashfree.lib.pg.constants.PgConstants;
import com.cashfree.lib.pg.domains.request.ListRefundsRequest;
import com.cashfree.lib.pg.domains.response.GetRefundResponse;
import com.cashfree.lib.pg.domains.response.ListRefundsResponse;
import com.cashfree.lib.pg.domains.request.InitiateRefundRequest;
import com.cashfree.lib.pg.domains.response.TriggerOrderRefundResponse;

public class Refunds {
  private Pg pg;

  public Refunds(Pg pg) {
    this.pg = pg;
  }

  public TriggerOrderRefundResponse triggerOrderRefund(InitiateRefundRequest request) {
    return pg.performPostRequest(
        PgConstants.ORDER_REFUND_REL_URL, request, InitiateRefundRequest.class, TriggerOrderRefundResponse.class);
  }

  public TriggerOrderRefundResponse triggerInstantOrderRefund(InitiateRefundRequest request) {
    request.setRefundType("INSTANT");
    if (CommonUtils.isBlank(request.getMode())) {
      throw new IllegalArgumentException("mode is a required parameter for instant refunds.");
    }
    return pg.performPostRequest(
        PgConstants.ORDER_REFUND_REL_URL, request, InitiateRefundRequest.class, TriggerOrderRefundResponse.class);
  }

  public ListRefundsResponse getRefundsForMerchant(ListRefundsRequest request) {
    return pg.performPostRequest(
        PgConstants.REFUNDS_REL_URL, request, ListRefundsRequest.class, ListRefundsResponse.class);
  }

  public GetRefundResponse getRefund(String refundId, String merchantRefundId) {
    Map<String, String> map = new HashMap<>();
    map.put("refundId", refundId);
    map.put("merchantRefundId", merchantRefundId);
    return pg.performPostRequest(PgConstants.REFUND_STATUS_REL_URL, map, GetRefundResponse.class);
  }
}
