package com.cashfree.lib.clients;

import org.springframework.http.HttpStatus;

import com.cashfree.lib.exceptions.UnknownExceptionOccured;
import com.cashfree.lib.exceptions.ResourceDoesntExistException;
import com.cashfree.lib.exceptions.ResourceAlreadyExistsException;

import com.cashfree.lib.domains.BeneficiaryDetails;
import com.cashfree.lib.domains.response.CfPayoutsResponse;
import com.cashfree.lib.domains.request.BulkValidationRequest;
import com.cashfree.lib.domains.response.BulkValidationResponse;
import com.cashfree.lib.domains.response.BulkValidationStatusResponse;
import com.cashfree.lib.domains.response.BankDetailsValidationResponse;

import com.cashfree.lib.constants.PayoutConstants;

public class Validation {
  private Payouts payouts;

  public Validation(Payouts payouts) {
    this.payouts = payouts;
  }

  public BankDetailsValidationResponse.Payload
  validateBankDetails(String name, String phone, String bankAccount, String ifsc) {
    BankDetailsValidationResponse body = payouts.performGetRequest(
        PayoutConstants.VALIDATION_BANK_DETAILS_REL_URL,
        BankDetailsValidationResponse.class, name, phone, bankAccount, ifsc);
    if (HttpStatus.OK.value() == body.getSubCode()) {
      return body.getData();
    } else if (HttpStatus.PRECONDITION_FAILED.value() == body.getSubCode()) {
      throw new ResourceDoesntExistException("Please provide a valid IFSC code");
    }
    throw new UnknownExceptionOccured();
  }

  public boolean validateUPIDetails(String name, String vpa) {
    CfPayoutsResponse body = payouts.performGetRequest(
        PayoutConstants.VALIDATION_UPI_DETAILS_REL_URL,
        CfPayoutsResponse.class, name, vpa);
    if (HttpStatus.OK.value() == body.getSubCode()) {
      return true;
    } else if (HttpStatus.PRECONDITION_FAILED.value() == body.getSubCode()) {
      throw new ResourceDoesntExistException("Either VPA or name is invalid");
    }
    throw new UnknownExceptionOccured();
  }

  public String validateBulkBankActivation(BulkValidationRequest bulkValidationRequest) {
    BulkValidationResponse body = payouts.performPostRequest(
        PayoutConstants.BULK_VALIDATION_BANK_DETAILS_REL_URL,
        bulkValidationRequest,
        BulkValidationResponse.class);
    if (HttpStatus.OK.value() == body.getSubCode()) {
      return body.getData().getBulkValidationId();
    } else if (HttpStatus.CONFLICT.value() == body.getSubCode()) {
      throw new ResourceAlreadyExistsException("Bulk Validation Id already exists");
    } else if (HttpStatus.PRECONDITION_FAILED.value() == body.getSubCode()) {
      throw new ResourceAlreadyExistsException("Mandatory Parameters missing in the request");
    }
    throw new UnknownExceptionOccured();
  }

  public BulkValidationStatusResponse getBulkValidationStatus(BeneficiaryDetails beneficiary) {
    BulkValidationStatusResponse body = payouts.performGetRequest(
        PayoutConstants.ADD_BENEFICIARY_REL_URL,
        BulkValidationStatusResponse.class);
    if (HttpStatus.OK.value() == body.getSubCode()) {
      return body;
    } else if (HttpStatus.NOT_FOUND.value() == body.getSubCode()) {
      throw new ResourceDoesntExistException("Bulk Validation Id does not exist");
    }
    throw new UnknownExceptionOccured();
  }
}
