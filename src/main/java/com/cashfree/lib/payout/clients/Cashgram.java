package com.cashfree.lib.payout.clients;

import com.cashfree.lib.payout.domains.CashgramDetails;
import com.cashfree.lib.payout.domains.response.CfPayoutsResponse;
import com.cashfree.lib.payout.domains.request.DeactivateCashgramRequest;
import com.cashfree.lib.payout.domains.response.CashgramCreationResponse;
import com.cashfree.lib.payout.domains.response.GetCashgramStatusResponse;

import com.cashfree.lib.exceptions.IllegalPayloadException;
import com.cashfree.lib.exceptions.ResourceDoesntExistException;
import com.cashfree.lib.exceptions.ResourceAlreadyExistsException;

import com.cashfree.lib.http.UriBuilder;
import com.cashfree.lib.payout.constants.PayoutConstants;

public class Cashgram {
  private Payouts payouts;

  public Cashgram(Payouts payouts) {
    this.payouts = payouts;
  }

  public CashgramCreationResponse createCashgram(CashgramDetails cashgram) {
    CashgramCreationResponse body = payouts.performPostRequest(
        PayoutConstants.CREATE_CASHGRAM_REL_URL,
        cashgram,
        CashgramCreationResponse.class);
    if (200 == body.getSubCode()) {
      return body;
    } else if (409 == body.getSubCode()) {
      throw new ResourceAlreadyExistsException("Cashgram with id " + cashgram.getCashgramId() + " already exists");
    } else if (422 == body.getSubCode()) {
      throw new IllegalPayloadException(body.getMessage());
    }
    throw new UnsupportedOperationException("Unable to create cashgram.");
  }

  public GetCashgramStatusResponse getCashgramStatus(String cashgramId) {
    UriBuilder uri = UriBuilder.fromUriString(PayoutConstants.GET_CASHGRAM_STATUS_REL_URL)
        .queryParam("cashgramId", cashgramId);

    GetCashgramStatusResponse body = payouts.performGetRequest(uri.toUriString(), GetCashgramStatusResponse.class);
    if (200 == body.getSubCode()) {
      return body;
    } else if (404 == body.getSubCode()) {
      throw new ResourceDoesntExistException("Cashgram with id " + cashgramId + " does not exists");
    }
    throw new UnsupportedOperationException("Unable to fetch cashgram for id " + cashgramId);
  }

  public CfPayoutsResponse deactivateCashgram(String cashgramId) {
    CfPayoutsResponse body = payouts.performPostRequest(
        PayoutConstants.DEACTIVATE_CASHGRAM_REL_URL,
        new DeactivateCashgramRequest().setCashgramId(cashgramId),
        CfPayoutsResponse.class);
    if (200 == body.getSubCode()) {
      return body;
    } else if (412 == body.getSubCode()) {
      throw new IllegalPayloadException("Cashgram with id " + cashgramId + " has already been expired");
    } else if (404 == body.getSubCode()) {
      throw new ResourceDoesntExistException("Cashgram with id " + cashgramId + " does not exists");
    }
    throw new UnsupportedOperationException("Unable to fetch cashgram for id, " + cashgramId);
  }
}
