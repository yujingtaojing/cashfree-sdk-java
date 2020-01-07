package com.cashfree.lib.clients;

import org.springframework.http.HttpStatus;
import org.springframework.web.util.UriComponentsBuilder;

import com.cashfree.lib.domains.CashgramDetails;
import com.cashfree.lib.domains.request.DeactivateCashgramRequest;
import com.cashfree.lib.domains.response.CashgramCreationResponse;
import com.cashfree.lib.domains.response.GetCashgramStatusResponse;

import com.cashfree.lib.exceptions.IllegalPayloadException;
import com.cashfree.lib.domains.response.CfPayoutsResponse;
import com.cashfree.lib.exceptions.ResourceDoesntExistException;
import com.cashfree.lib.exceptions.ResourceAlreadyExistsException;

import com.cashfree.lib.constants.PayoutConstants;

public class Cashgram {
  private Payouts payouts;

  public Cashgram(Payouts payouts) {
    this.payouts = payouts;
  }

  public CashgramCreationResponse.Payload createCashgram(CashgramDetails cashgram) {
    CashgramCreationResponse body = payouts.performPostRequest(
        PayoutConstants.CREATE_CASHGRAM_REL_URL,
        cashgram,
        CashgramCreationResponse.class);
    if (HttpStatus.OK.value() == body.getSubCode()) {
      return body.getData();
    } else if (HttpStatus.CONFLICT.value() == body.getSubCode()) {
      throw new ResourceAlreadyExistsException("Cashgram with id " + cashgram.getCashgramId() + " already exists");
    } else if (HttpStatus.UNPROCESSABLE_ENTITY.value() == body.getSubCode()) {
      throw new IllegalPayloadException(body.getMessage());
    }
    throw new UnsupportedOperationException("Unable to create cashgram.");
  }

  public GetCashgramStatusResponse.Payload getCashgramStatus(String cashgramId) {
    UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(PayoutConstants.GET_CASHGRAM_STATUS_REL_URL)
        .queryParam("cashgramId", cashgramId);

    GetCashgramStatusResponse body = payouts.performGetRequest(uri.toUriString(), GetCashgramStatusResponse.class);
    if (HttpStatus.OK.value() == body.getSubCode()) {
      return body.getData();
    } else if (HttpStatus.NOT_FOUND.value() == body.getSubCode()) {
      throw new ResourceDoesntExistException("Cashgram with id " + cashgramId + " does not exists");
    }
    throw new UnsupportedOperationException("Unable to fetch cashgram for id " + cashgramId);
  }

  public boolean deactivateCashgram(String cashgramId) {
    CfPayoutsResponse body = payouts.performPostRequest(
        PayoutConstants.DEACTIVATE_CASHGRAM_REL_URL,
        new DeactivateCashgramRequest().setCashgramId(cashgramId),
        CfPayoutsResponse.class);
    if (HttpStatus.OK.value() == body.getSubCode()) {
      return true;
    } else if (HttpStatus.PRECONDITION_FAILED.value() == body.getSubCode()) {
      throw new IllegalPayloadException("Cashgram with id " + cashgramId + " has already been expired");
    } else if (HttpStatus.NOT_FOUND.value() == body.getSubCode()) {
      throw new ResourceDoesntExistException("Cashgram with id " + cashgramId + " does not exists");
    }
    throw new UnsupportedOperationException("Unable to fetch cashgram for id, " + cashgramId);
  }
}
