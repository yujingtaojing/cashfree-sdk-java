package com.cashfree.lib.payout.clients;

import com.cashfree.lib.exceptions.IllegalPayloadException;
import com.cashfree.lib.exceptions.ResourceAlreadyExistsException;
import com.cashfree.lib.exceptions.ResourceDoesntExistException;
import com.cashfree.lib.exceptions.UnknownExceptionOccured;
import com.cashfree.lib.payout.constants.PayoutConstants;
import com.cashfree.lib.payout.domains.BeneficiaryDetails;
import com.cashfree.lib.payout.domains.request.RemoveBeneficiaryRequest;
import com.cashfree.lib.payout.domains.response.CfPayoutsResponse;
import com.cashfree.lib.payout.domains.response.GetBeneficiaryIdResponse;
import com.cashfree.lib.payout.domains.response.GetBeneficiaryResponse;
import com.cashfree.lib.utils.ExceptionThrower;
import org.apache.http.client.utils.URIBuilder;

public class Beneficiary {
  private Payouts payouts;

  public Beneficiary(Payouts payouts) {
    this.payouts = payouts;
  }

  public boolean addBeneficiary(BeneficiaryDetails beneficiary) {
    URIBuilder uriBuilder = null;
    try {
      uriBuilder =
              new URIBuilder(Payouts.getEndpoint() + PayoutConstants.ADD_BENEFICIARY_REL_URL);
    } catch (Exception e) {
      e.printStackTrace();
    }
    CfPayoutsResponse body = payouts.performPostRequest(
            uriBuilder.toString(),
        beneficiary,
        CfPayoutsResponse.class);
    String msg = "Message : "+ body.getMessage() + " | X-Request-Id: " + body.getRequestId();

    if (200 == body.getSubCode() || 201 == body.getSubCode() || 202 == body.getSubCode()) {
      return true;
    } else if (409 == body.getSubCode()) {
      throw new ResourceAlreadyExistsException("Beneficiary Id already exists.\n" +  msg);
    } else if (412 == body.getSubCode()) {
      throw new UnknownExceptionOccured("Post data is empty or not a valid JSON.\n" +  msg);
    } else if (422 == body.getSubCode()) {
      throw new IllegalArgumentException("Please provide a valid Beneficiary Id.\n" +  msg);
    }
    else{
      ExceptionThrower.throwException(body.getSubCode() , body.getRequestId(), body.getMessage());
    }
    return false;
  }

  public GetBeneficiaryResponse.Payload getBeneficiaryDetails(String beneId) {
    URIBuilder uriBuilder = null;
    try {
      uriBuilder =
              new URIBuilder(Payouts.getEndpoint() + PayoutConstants.GET_BENEFICIARY_REL_URL + "/" + beneId);
    } catch (Exception e) {
      e.printStackTrace();
    }
    GetBeneficiaryResponse body = payouts.performGetRequest(
            uriBuilder.toString(), GetBeneficiaryResponse.class);
    String msg = "Message : "+ body.getMessage() + " | X-Request-Id: " + body.getRequestId();
    if (200 == body.getSubCode() || 201 == body.getSubCode() || 202 == body.getSubCode()) {
      return body.getData();
    } else if (404 == body.getSubCode()) {
      throw new ResourceDoesntExistException("Beneficiary does not exist\n" +  msg);
    }
      ExceptionThrower.throwException(body.getSubCode() ,
              body.getRequestId(),
              "Unable to fetch Benificiary details. " +body.getMessage());
    return  body.getData();
  }

  public String getBeneficiaryId(String bankAccount, String ifsc) {
    URIBuilder uriBuilder = null;
    try {
      uriBuilder =
              new URIBuilder(Payouts.getEndpoint() + PayoutConstants.GET_BENE_ID_REL_URL);
    } catch (Exception e) {
      e.printStackTrace();
    }

    uriBuilder.addParameter("bankAccount", bankAccount);
    uriBuilder.addParameter("ifsc", ifsc);

    GetBeneficiaryIdResponse body = payouts.performGetRequest(
            uriBuilder.toString(), GetBeneficiaryIdResponse.class);
    String msg = "Message : "+ body.getMessage() + " | X-Request-Id: " + body.getRequestId();
    if (200 == body.getSubCode() || 201 == body.getSubCode() || 202 == body.getSubCode()) {
      return body.getData().getBeneId();
    } else if (404 == body.getSubCode() || 403 == body.getSubCode()) {
      throw new ResourceDoesntExistException("Beneficiary not found with given bank account details.\n" +  msg);
    }
    ExceptionThrower.throwException(body.getSubCode() ,
            body.getRequestId(),
            "Unable to fetch Benificiary id. " +body.getMessage());
    return "";
  }

  public boolean removeBeneficiary(String beneId) {
    URIBuilder uriBuilder = null;
    try {
      uriBuilder =
              new URIBuilder(Payouts.getEndpoint() + PayoutConstants.REMOVE_BENEFICIARY_REL_URL);
    } catch (Exception e) {
      e.printStackTrace();
    }
    CfPayoutsResponse body = payouts.performPostRequest(
            uriBuilder.toString(), new RemoveBeneficiaryRequest().setBeneId(beneId), CfPayoutsResponse.class);
    String msg = "Message : "+ body.getMessage() + " | X-Request-Id: " + body.getRequestId();
    if (200 == body.getSubCode() || 201 == body.getSubCode() || 202 == body.getSubCode()) {
      return true;
    } else if (404 == body.getSubCode()) {
      throw new ResourceDoesntExistException("Beneficiary does not exist with given Id\n" +  msg);
    } else if (412 == body.getSubCode()) {
      throw new IllegalPayloadException("beneId missing in the request\n" +  msg);
    }
    ExceptionThrower.throwException(body.getSubCode() ,
            body.getRequestId(),("Unable to remove beneficiary with id " + beneId + "\n" + body.getMessage()));
    return false;
  }
}
