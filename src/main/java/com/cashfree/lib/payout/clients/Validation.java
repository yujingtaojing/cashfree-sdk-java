package com.cashfree.lib.payout.clients;

import com.cashfree.lib.exceptions.UnknownExceptionOccured;
import com.cashfree.lib.exceptions.ResourceDoesntExistException;
import com.cashfree.lib.exceptions.ResourceAlreadyExistsException;

import com.cashfree.lib.payout.domains.request.BulkValidationRequest;
import com.cashfree.lib.payout.domains.response.UPIValidationResponse;
import com.cashfree.lib.payout.domains.response.BulkValidationResponse;
import com.cashfree.lib.payout.domains.response.BulkValidationStatusResponse;
import com.cashfree.lib.payout.domains.response.BankDetailsValidationResponse;

import com.cashfree.lib.http.UriBuilder;
import com.cashfree.lib.utils.CommonUtils;
import com.cashfree.lib.payout.constants.PayoutConstants;

public class Validation {
  private Payouts payouts;

  public Validation(Payouts payouts) {
    this.payouts = payouts;
  }

  public BankDetailsValidationResponse.Payload
  validateBankDetails(String name, String phone, String bankAccount, String ifsc) {
    UriBuilder uri = UriBuilder.fromUriString(PayoutConstants.VALIDATION_BANK_DETAILS_REL_URL)
        .queryParam("name", name)
        .queryParam("phone", phone)
        .queryParam("bankAccount", bankAccount)
        .queryParam("ifsc", ifsc);

    BankDetailsValidationResponse body = payouts.performGetRequest(uri.toUriString(), BankDetailsValidationResponse.class);
    if (200 == body.getSubCode()) {
      return body.getData();
    } else if (412 == body.getSubCode()) {
      throw new ResourceDoesntExistException("Please provide a valid IFSC code");
    }
    throw new UnknownExceptionOccured(body.getMessage());
  }

  public UPIValidationResponse.Payload
  validateUPIDetails(String name, String vpa) {
    UriBuilder uri = UriBuilder.fromUriString(PayoutConstants.VALIDATION_UPI_DETAILS_REL_URL)
        .queryParam("name", name)
        .queryParam("vpa", vpa);

    UPIValidationResponse body = payouts.performGetRequest(uri.toUriString(), UPIValidationResponse.class);
    if (200 == body.getSubCode()) {
      return body.getData();
    } else if (412 == body.getSubCode()) {
      throw new ResourceDoesntExistException("Either VPA or name is invalid");
    }
    throw new UnknownExceptionOccured();
  }

  public BulkValidationResponse validateBulkBankActivation(BulkValidationRequest bulkValidationRequest) {
    BulkValidationResponse body = payouts.performPostRequest(
        PayoutConstants.BULK_VALIDATION_BANK_DETAILS_REL_URL,
        bulkValidationRequest,
        BulkValidationResponse.class);
    if (200 == body.getSubCode()) {
      return body;
    } else if (409 == body.getSubCode()) {
      throw new ResourceAlreadyExistsException("Bulk Validation Id already exists");
    } else if (412 == body.getSubCode()) {
      throw new ResourceAlreadyExistsException("Mandatory Parameters missing in the request");
    }
    throw new UnknownExceptionOccured(body.getMessage());
  }

  public BulkValidationStatusResponse.Payload
  getBulkValidationStatus(String bulkValidationId, String bankAccount, String ifsc) {
    UriBuilder uri = UriBuilder.fromUriString(PayoutConstants.GET_BULK_VALIDATION_STATUS_REL_URL)
        .queryParam("bulkValidationId", bulkValidationId);
    if (CommonUtils.isNotBlank(bankAccount)) {
      uri.queryParam("bankAccount", bankAccount);
    }
    if (CommonUtils.isNotBlank(ifsc)) {
      uri.queryParam("ifsc", ifsc);
    }

    BulkValidationStatusResponse body = payouts.performGetRequest(uri.toUriString(), BulkValidationStatusResponse.class);
    if (200 == body.getSubCode()) {
      return body.getData();
    } else if (404 == body.getSubCode()) {
      throw new ResourceDoesntExistException("Bulk Validation Id does not exist");
    }
    throw new UnknownExceptionOccured();
  }
}
