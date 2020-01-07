package com.cashfree.lib.clients;

import org.springframework.http.HttpStatus;
import org.springframework.web.util.UriComponentsBuilder;

import org.apache.commons.lang3.StringUtils;

import com.cashfree.lib.exceptions.UnknownExceptionOccured;
import com.cashfree.lib.exceptions.ResourceDoesntExistException;
import com.cashfree.lib.exceptions.ResourceAlreadyExistsException;

import com.cashfree.lib.domains.request.BulkValidationRequest;
import com.cashfree.lib.domains.response.UPIValidationResponse;
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
    UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(PayoutConstants.VALIDATION_BANK_DETAILS_REL_URL)
        .queryParam("name", name)
        .queryParam("phone", phone)
        .queryParam("bankAccount", bankAccount)
        .queryParam("ifsc", ifsc);

    BankDetailsValidationResponse body = payouts.performGetRequest(uri.toUriString(), BankDetailsValidationResponse.class);
    if (HttpStatus.OK.value() == body.getSubCode()) {
      return body.getData();
    } else if (HttpStatus.PRECONDITION_FAILED.value() == body.getSubCode()) {
      throw new ResourceDoesntExistException("Please provide a valid IFSC code");
    }
    throw new UnknownExceptionOccured();
  }

  public UPIValidationResponse.Payload
  validateUPIDetails(String name, String vpa) {
    UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(PayoutConstants.VALIDATION_UPI_DETAILS_REL_URL)
        .queryParam("name", name)
        .queryParam("vpa", vpa);

    UPIValidationResponse body = payouts.performGetRequest(uri.toUriString(), UPIValidationResponse.class);
    if (HttpStatus.OK.value() == body.getSubCode()) {
      return body.getData();
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

  public BulkValidationStatusResponse.Payload
  getBulkValidationStatus(String bulkValidationId, String bankAccount, String ifsc) {
    UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(PayoutConstants.GET_BULK_VALIDATION_STATUS_REL_URL)
        .queryParam("bulkValidationId", bulkValidationId);
    if (StringUtils.isNotBlank(bankAccount)) {
      uri.queryParam("bankAccount", bankAccount);
    }
    if (StringUtils.isNotBlank(ifsc)) {
      uri.queryParam("ifsc", ifsc);
    }

    BulkValidationStatusResponse body = payouts.performGetRequest(uri.toUriString(), BulkValidationStatusResponse.class);
    System.out.println(body);
    if (HttpStatus.OK.value() == body.getSubCode()) {
      return body.getData();
    } else if (HttpStatus.NOT_FOUND.value() == body.getSubCode()) {
      throw new ResourceDoesntExistException("Bulk Validation Id does not exist");
    }
    throw new UnknownExceptionOccured();
  }
}
