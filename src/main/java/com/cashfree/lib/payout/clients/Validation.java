package com.cashfree.lib.payout.clients;

import com.cashfree.lib.exceptions.UnknownExceptionOccured;
import com.cashfree.lib.exceptions.ResourceDoesntExistException;
import com.cashfree.lib.exceptions.ResourceAlreadyExistsException;

import com.cashfree.lib.payout.domains.request.BulkValidationRequest;
import com.cashfree.lib.payout.domains.response.UPIValidationResponse;
import com.cashfree.lib.payout.domains.response.BulkValidationResponse;
import com.cashfree.lib.payout.domains.response.BulkValidationStatusResponse;
import com.cashfree.lib.payout.domains.response.BankDetailsValidationResponse;

import com.cashfree.lib.utils.CommonUtils;
import com.cashfree.lib.payout.constants.PayoutConstants;
import com.cashfree.lib.utils.ExceptionThrower;
import org.apache.http.client.utils.URIBuilder;

public class Validation {
  private Payouts payouts;

  public Validation(Payouts payouts) {
    this.payouts = payouts;
  }

  public BankDetailsValidationResponse.Payload validateBankDetails(
      String name, String phone, String bankAccount, String ifsc) {
    URIBuilder uriBuilder = null;
    try {
      uriBuilder =
          new URIBuilder(Payouts.getEndpoint() + PayoutConstants.VALIDATION_BANK_DETAILS_REL_URL);
    } catch (Exception e) {
      e.printStackTrace();
    }
    uriBuilder.addParameter("name", name);
    uriBuilder.addParameter("phone", phone);
    uriBuilder.addParameter("bankAccount", bankAccount);
    uriBuilder.addParameter("ifsc", ifsc);
    BankDetailsValidationResponse body =
        payouts.performGetRequest(uriBuilder.toString(), BankDetailsValidationResponse.class);
    if (200 == body.getSubCode() || 201 == body.getSubCode() || 202 == body.getSubCode()) {
      return body.getData();
    } else if (412 == body.getSubCode()) {
      throw new ResourceDoesntExistException("Please provide a valid IFSC code");
    }
    ExceptionThrower.throwException(body.getSubCode() ,
            body.getRequestId(),
            body.getMessage());
    return body.getData();
  }

  public UPIValidationResponse.Payload validateUPIDetails(String name, String vpa) {
    URIBuilder uriBuilder = null;
    try {
      uriBuilder =
          new URIBuilder(Payouts.getEndpoint() + PayoutConstants.VALIDATION_UPI_DETAILS_REL_URL);
    } catch (Exception e) {
      e.printStackTrace();
    }
    uriBuilder.addParameter("name", name);
    uriBuilder.addParameter("vpa", vpa);
    UPIValidationResponse body =
        payouts.performGetRequest(uriBuilder.toString(), UPIValidationResponse.class);
    if (200 == body.getSubCode() || 201 == body.getSubCode() || 202 == body.getSubCode()) {
      return body.getData();
    } else if (412 == body.getSubCode()) {
      throw new ResourceDoesntExistException("Either VPA or name is invalid");
    }
    ExceptionThrower.throwException(body.getSubCode() ,
            body.getRequestId(),
            body.getMessage());
    return body.getData();
  }
  public BulkValidationResponse validateBulkBankActivation(BulkValidationRequest bulkValidationRequest) {
    URIBuilder uriBuilder = null;
    try {
      uriBuilder =
              new URIBuilder(Payouts.getEndpoint() + PayoutConstants.BULK_VALIDATION_BANK_DETAILS_REL_URL);
    } catch (Exception e) {
      e.printStackTrace();
    }
    BulkValidationResponse body = payouts.performPostRequest(
            uriBuilder.toString(),
        bulkValidationRequest,
        BulkValidationResponse.class);
    if (200 == body.getSubCode() || 201 == body.getSubCode() || 202 == body.getSubCode()) {
      return body;
    } else if (409 == body.getSubCode()) {
      throw new ResourceAlreadyExistsException("Bulk Validation Id already exists");
    } else if (412 == body.getSubCode()) {
      throw new ResourceAlreadyExistsException("Mandatory Parameters missing in the request");
    }
    ExceptionThrower.throwException(body.getSubCode() ,
            body.getRequestId(),
            body.getMessage());
    return body;
  }


  public BulkValidationStatusResponse.Payload
  getBulkValidationStatus(String bulkValidationId, String bankAccount, String ifsc) {
    URIBuilder uriBuilder = null;
    try {
      uriBuilder =
              new URIBuilder(Payouts.getEndpoint() + PayoutConstants.GET_BULK_VALIDATION_STATUS_REL_URL);
    } catch (Exception e) {
      e.printStackTrace();
    }
    uriBuilder.addParameter("bulkValidationId", bulkValidationId);
    if (CommonUtils.isNotBlank(bankAccount)) {
      uriBuilder.addParameter("bankAccount", bankAccount);
    }
    if (CommonUtils.isNotBlank(ifsc)) {
      uriBuilder.addParameter("ifsc", ifsc);
    }

    BulkValidationStatusResponse body = payouts.performGetRequest(uriBuilder.toString(), BulkValidationStatusResponse.class);
    if (200 == body.getSubCode() || 201 == body.getSubCode() || 202 == body.getSubCode()) {
      return body.getData();
    } else if (404 == body.getSubCode()) {
      throw new ResourceDoesntExistException("Bulk Validation Id does not exist");
    }
    ExceptionThrower.throwException(body.getSubCode() ,
            body.getRequestId(),
            body.getMessage());
    return  body.getData();
  }
}
