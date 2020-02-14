package com.cashfree.lib.pg.clients;

import com.cashfree.lib.pg.constants.PgConstants;
import com.cashfree.lib.pg.domains.request.ListTransactionsRequest;
import com.cashfree.lib.pg.domains.response.ListTransactionsResponse;

public class Transactions {
  private Pg pg;

  public Transactions(Pg pg) {
    this.pg = pg;
  }

  public ListTransactionsResponse getTransactions(ListTransactionsRequest request) {
    return pg.performPostRequest(
        PgConstants.TRANSACTIONS_REL_URL, request, ListTransactionsRequest.class, ListTransactionsResponse.class);
  }
}
