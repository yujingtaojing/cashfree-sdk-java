package com.cashfree.lib.payout.clients;

import com.cashfree.lib.exceptions.UnknownExceptionOccured;
import com.cashfree.lib.exceptions.IllegalPayloadException;
import com.cashfree.lib.exceptions.ResourceDoesntExistException;
import com.cashfree.lib.exceptions.ResourceAlreadyExistsException;

import com.cashfree.lib.payout.domains.BeneficiaryDetails;
import com.cashfree.lib.payout.domains.response.CfPayoutsResponse;
import com.cashfree.lib.payout.domains.request.RemoveBeneficiaryRequest;
import com.cashfree.lib.payout.domains.response.GetBeneficiaryResponse;
import com.cashfree.lib.payout.domains.response.GetBeneficiaryIdResponse;

import com.cashfree.lib.http.UriBuilder;
import com.cashfree.lib.payout.constants.PayoutConstants;

public class Beneficiary {
  private Payouts payouts;

  public Beneficiary(Payouts payouts) {
    this.payouts = payouts;
  }

  public boolean addBeneficiary(BeneficiaryDetails beneficiary) {
    CfPayoutsResponse body = payouts.performPostRequest(
        PayoutConstants.ADD_BENEFICIARY_REL_URL,
        beneficiary,
        CfPayoutsResponse.class);
    if (200 == body.getSubCode()) {
      return true;
    } else if (409 == body.getSubCode()) {
      throw new ResourceAlreadyExistsException("Beneficiary Id already exists");
    } else if (412 == body.getSubCode()) {
      throw new UnknownExceptionOccured("Post data is empty or not a valid JSON.");
    } else if (422 == body.getSubCode()) {
      throw new IllegalArgumentException("Please provide a valid Beneficiary Id.");
    }
    return false;
  }

  public BeneficiaryDetails getBeneficiaryDetails(String beneId) {
    GetBeneficiaryResponse body = payouts.performGetRequest(
        PayoutConstants.GET_BENEFICIARY_REL_URL + "/" + beneId, GetBeneficiaryResponse.class);
    if (200 == body.getSubCode()) {
      return body.getData().getBeneficiaryDetails();
    } else if (404 == body.getSubCode()) {
      throw new ResourceDoesntExistException("Beneficiary does not exist");
    }
    throw new UnknownExceptionOccured("Unable to fetch beneficiary details");
  }

  public String getBeneficiaryId(String bankAccount, String ifsc) {

    UriBuilder uri = UriBuilder.fromUriString(PayoutConstants.GET_BENE_ID_REL_URL)
        .queryParam("bankAccount", bankAccount)
        .queryParam("ifsc", ifsc);

    GetBeneficiaryIdResponse body = payouts.performGetRequest(
        uri.toUriString(), GetBeneficiaryIdResponse.class);

    if (200 == body.getSubCode()) {
      return body.getData().getBeneId();
    } else if (403 == body.getSubCode()) {
      throw new ResourceDoesntExistException("Beneficiary not found with given bank account details");
    }
    throw new UnknownExceptionOccured("Unable to fetch beneficiary id");
  }

  public boolean removeBeneficiary(String beneId) {
    CfPayoutsResponse body = payouts.performPostRequest(
        PayoutConstants.REMOVE_BENEFICIARY_REL_URL, new RemoveBeneficiaryRequest().setBeneId(beneId), CfPayoutsResponse.class);

    if (200 == body.getSubCode()) {
      return true;
    } else if (404 == body.getSubCode()) {
      throw new ResourceDoesntExistException("Beneficiary does not exist with given Id");
    } else if (412 == body.getSubCode()) {
      throw new IllegalPayloadException("beneId missing in the request");
    }
    throw new UnknownExceptionOccured("Unable to remove beneficiary with id " + beneId);
  }
}
