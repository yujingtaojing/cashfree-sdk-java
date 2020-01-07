package com.cashfree.lib.clients;

import org.springframework.http.HttpStatus;
import org.springframework.web.util.UriComponentsBuilder;

import com.cashfree.lib.exceptions.UnknownExceptionOccured;
import com.cashfree.lib.exceptions.ResourceDoesntExistException;
import com.cashfree.lib.exceptions.ResourceAlreadyExistsException;

import com.cashfree.lib.domains.BeneficiaryDetails;
import com.cashfree.lib.exceptions.IllegalPayloadException;
import com.cashfree.lib.domains.response.CfPayoutsResponse;
import com.cashfree.lib.domains.request.RemoveBeneficiaryRequest;
import com.cashfree.lib.domains.response.GetBeneficiaryResponse;
import com.cashfree.lib.domains.response.GetBeneficiaryIdResponse;

import com.cashfree.lib.constants.PayoutConstants;

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
    if (HttpStatus.OK.value() == body.getSubCode()) {
      return true;
    } else if (HttpStatus.CONFLICT.value() == body.getSubCode()) {
      throw new ResourceAlreadyExistsException("Beneficiary Id already exists");
    } else if (HttpStatus.PRECONDITION_FAILED.value() == body.getSubCode()) {
      throw new UnknownExceptionOccured("Post data is empty or not a valid JSON.");
    } else if (HttpStatus.UNPROCESSABLE_ENTITY.value() == body.getSubCode()) {
      throw new IllegalArgumentException("Please provide a valid Beneficiary Id.");
    }
    return false;
  }

  public BeneficiaryDetails getBeneficiaryDetails(String beneId) {
    GetBeneficiaryResponse body = payouts.performGetRequest(
        PayoutConstants.GET_BENEFICIARY_REL_URL + "/" + beneId, GetBeneficiaryResponse.class);
    if (HttpStatus.OK.value() == body.getSubCode()) {
      return body.getData().getBeneficiaryDetails();
    } else if (HttpStatus.NOT_FOUND.value() == body.getSubCode()) {
      throw new ResourceDoesntExistException("Beneficiary does not exist");
    }
    throw new UnknownExceptionOccured("Unable to fetch beneficiary details");
  }

  public String getBeneficiaryId(String bankAccount, String ifsc) {
    UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(PayoutConstants.GET_BENE_ID_REL_URL)
        .queryParam("bankAccount", bankAccount)
        .queryParam("ifsc", ifsc);

    GetBeneficiaryIdResponse body = payouts.performGetRequest(
        uri.toUriString(), GetBeneficiaryIdResponse.class);
    System.out.println(body);
    if (HttpStatus.OK.value() == body.getSubCode()) {
      return body.getData().getBeneId();
    } else if (HttpStatus.FORBIDDEN.value() == body.getSubCode()) {
      throw new ResourceDoesntExistException("Beneficiary not found with given bank account details");
    }
    throw new UnknownExceptionOccured("Unable to fetch beneficiary id");
  }

  public boolean removeBeneficiary(String beneId) {
    CfPayoutsResponse body = payouts.performPostRequest(
        PayoutConstants.REMOVE_BENEFICIARY_REL_URL, new RemoveBeneficiaryRequest().setBeneId(beneId), CfPayoutsResponse.class);
    System.out.println(body);
    if (HttpStatus.OK.value() == body.getSubCode()) {
      return true;
    } else if (HttpStatus.NOT_FOUND.value() == body.getSubCode()) {
      throw new ResourceDoesntExistException("Beneficiary does not exist with given Id");
    } else if (HttpStatus.PRECONDITION_FAILED.value() == body.getSubCode()) {
      throw new IllegalPayloadException("beneId missing in the request");
    }
    throw new UnknownExceptionOccured("Unable to remove beneficiary with id " + beneId);
  }
}
