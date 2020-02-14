package com.cashfree.lib.pg.clients;

import java.util.Map;
import java.util.HashMap;

import com.cashfree.lib.utils.CommonUtils;
import com.cashfree.lib.pg.constants.PgConstants;
import com.cashfree.lib.pg.domains.request.ListSettlementsRequest;
import com.cashfree.lib.pg.domains.response.FetchSettlementResponse;
import com.cashfree.lib.pg.domains.response.ListSettlementsResponse;

public class Settlements {
  private Pg pg;

  public Settlements(Pg pg) {
    this.pg = pg;
  }

  public ListSettlementsResponse fetchAllSettlements(ListSettlementsRequest request) {
    return pg.performPostRequest(
        PgConstants.SETTLEMENTS_REL_URL, request, ListSettlementsRequest.class, ListSettlementsResponse.class);
  }

  public FetchSettlementResponse fetchSettlement(String settlementId, String lastId, Integer count) {
    if (CommonUtils.isBlank(settlementId)) {
      throw new IllegalArgumentException("SettlementId can't be blank or null.");
    }
    Map<String, String> map = new HashMap<>();
    map.put("settlementId", settlementId);
    map.put("lastId", lastId);
    map.put("count", count.toString());
    return pg.performPostRequest(PgConstants.SETTLEMENT_REL_URL, map, FetchSettlementResponse.class);
  }
}
