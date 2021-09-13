package com.cashfree.lib.payout.clients;

import com.cashfree.lib.payout.domains.CashgramDetails;
import com.cashfree.lib.payout.domains.response.CfPayoutsResponse;
import com.cashfree.lib.payout.domains.request.DeactivateCashgramRequest;
import com.cashfree.lib.payout.domains.response.CashgramCreationResponse;
import com.cashfree.lib.payout.domains.response.GetCashgramStatusResponse;

import com.cashfree.lib.exceptions.IllegalPayloadException;
import com.cashfree.lib.exceptions.ResourceDoesntExistException;
import com.cashfree.lib.exceptions.ResourceAlreadyExistsException;

import com.cashfree.lib.utils.ExceptionThrower;
import org.apache.http.client.utils.URIBuilder;
import com.cashfree.lib.payout.constants.PayoutConstants;

public class Cashgram {
  private Payouts payouts;

  public Cashgram(Payouts payouts) {
    this.payouts = payouts;
  }

  public CashgramCreationResponse createCashgram(CashgramDetails cashgram) {
    URIBuilder uriBuilder = null;
    try {
      uriBuilder =
              new URIBuilder(Payouts.getEndpoint() + PayoutConstants.CREATE_CASHGRAM_REL_URL);
    } catch (Exception e) {
      e.printStackTrace();
    }
    CashgramCreationResponse body = payouts.performPostRequest(
            uriBuilder.toString(),
        cashgram,
        CashgramCreationResponse.class);
    String msg = "Message : "+ body.getMessage() + " | X-Request-Id: " + body.getXRequestId();
    if (200 == body.getSubCode() || 201 == body.getSubCode() || 202 == body.getSubCode()) {
      return body;
    } else if (409 == body.getSubCode()) {
      throw new ResourceAlreadyExistsException("Cashgram with id " + cashgram.getCashgramId() + " already exists");
    } else if (422 == body.getSubCode()) {
      throw new IllegalPayloadException(msg);
    }
    ExceptionThrower.throwException(body.getSubCode() ,
            body.getXRequestId(), "Unable to create cashgram." + body.getMessage());
    return body;
  }

  public GetCashgramStatusResponse getCashgramStatus(String cashgramId) {
    URIBuilder uriBuilder = null;
    try {
      uriBuilder =
              new URIBuilder(Payouts.getEndpoint() + PayoutConstants.GET_CASHGRAM_STATUS_REL_URL);
    } catch (Exception e) {
      e.printStackTrace();
    }
    uriBuilder.addParameter("cashgramId", cashgramId);

    GetCashgramStatusResponse body = payouts.performGetRequest(uriBuilder.toString(), GetCashgramStatusResponse.class);
    String msg = "Message : "+ body.getMessage() + " | X-Request-Id: " + body.getXRequestId();
    if (200 == body.getSubCode() || 201 == body.getSubCode() || 202 == body.getSubCode()) {
      return body;
    } else if (404 == body.getSubCode()) {
      throw new ResourceDoesntExistException("Cashgram with id " + cashgramId + " does not exists\n" + msg);
    }
    ExceptionThrower.throwException(body.getSubCode() ,
            body.getXRequestId(), "Unable to fetch cashgram for id " + cashgramId + "\n" + body.getMessage());
    return body;
  }

  public CfPayoutsResponse deactivateCashgram(String cashgramId) {
    URIBuilder uriBuilder = null;
    try {
      uriBuilder =
              new URIBuilder(Payouts.getEndpoint() + PayoutConstants.DEACTIVATE_CASHGRAM_REL_URL);
    } catch (Exception e) {
      e.printStackTrace();
    }
    CfPayoutsResponse body = payouts.performPostRequest(
            uriBuilder.toString(),
        new DeactivateCashgramRequest().setCashgramId(cashgramId),
        CfPayoutsResponse.class);
    String msg = "Message : "+ body.getMessage() + " | X-Request-Id: " + body.getXRequestId();
    if (200 == body.getSubCode() || 201 == body.getSubCode() || 202 == body.getSubCode()) {
      return body;
    } else if (412 == body.getSubCode()) {
      throw new IllegalPayloadException("Cashgram with id " + cashgramId + " has already been expired.\n"+ msg);
    } else if (404 == body.getSubCode()) {
      throw new ResourceDoesntExistException("Cashgram with id " + cashgramId + " does not exists.\n" + msg);
    }
    ExceptionThrower.throwException(body.getSubCode() ,
            body.getXRequestId(),
            "Unable to fetch cashgram for id, " + cashgramId + "\n" + body.getMessage());
    return body;
  }
}
