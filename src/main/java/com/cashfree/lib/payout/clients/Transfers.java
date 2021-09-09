package com.cashfree.lib.payout.clients;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.cashfree.lib.exceptions.IllegalPayloadException;
import com.cashfree.lib.exceptions.UnknownExceptionOccured;
import com.cashfree.lib.exceptions.ResourceDoesntExistException;
import com.cashfree.lib.exceptions.ResourceAlreadyExistsException;

import com.cashfree.lib.payout.domains.request.BatchTransferRequest;
import com.cashfree.lib.payout.domains.response.GetTransferResponse;
import com.cashfree.lib.payout.domains.response.GetTransfersResponse;
import com.cashfree.lib.payout.domains.response.BatchTransferResponse;
import com.cashfree.lib.payout.domains.request.RequestTransferRequest;
import com.cashfree.lib.payout.domains.response.RequestTransferResponse;
import com.cashfree.lib.payout.domains.response.BatchTransferStatusResponse;

import com.cashfree.lib.utils.ExceptionThrower;
import org.apache.http.client.utils.URIBuilder;
import com.cashfree.lib.utils.CommonUtils;
import com.cashfree.lib.payout.constants.PayoutConstants;

public class Transfers {
  private Payouts payouts;

  public Transfers(Payouts payouts) {
    this.payouts = payouts;
  }

  public RequestTransferResponse requestTransfer(RequestTransferRequest transferRequest) {
    URIBuilder uriBuilder = null;
    try {
      uriBuilder =
              new URIBuilder(Payouts.getEndpoint() + PayoutConstants.REQUEST_TRANSFER_REL_URL);
    } catch (Exception e) {
      e.printStackTrace();
    }
    RequestTransferResponse body = payouts.performPostRequest(
            uriBuilder.toString(),
        transferRequest,
        RequestTransferResponse.class);
    if (200 == body.getSubCode() || 201 == body.getSubCode() || 202 == body.getSubCode()) {
      return body;
    }
    ExceptionThrower.throwException(body.getSubCode() ,
            body.getRequestId(),
            body.getMessage());
    return body;
  }

  public GetTransferResponse getTransferStatus(String referenceId, String transferId) {
    if (CommonUtils.isBlank(referenceId) && CommonUtils.isBlank(transferId)) {
      throw new IllegalPayloadException("Either referenceId or transferId is mandatory to retrieve status of the transfer");
    }
    URIBuilder uriBuilder = null;
    try {
      uriBuilder =
              new URIBuilder(Payouts.getEndpoint() + PayoutConstants.GET_TRANSFER_STATUS_REL_URL);
    } catch (Exception e) {
      e.printStackTrace();
    }

    if (CommonUtils.isNotBlank(transferId)) {
      uriBuilder.addParameter("transferId", transferId);
    }
    if (CommonUtils.isNotBlank(referenceId)) {
      uriBuilder.addParameter("referenceId", referenceId);
    }
    GetTransferResponse body = payouts.performGetRequest(uriBuilder.toString(), GetTransferResponse.class);
    String msg = "Message : "+ body.getMessage() + " | X-Request-Id: " + body.getRequestId();
    if (200 == body.getSubCode() || 201 == body.getSubCode() || 202 == body.getSubCode()) {
      return body;
    } else if (404 == body.getSubCode()) {
      throw new ResourceDoesntExistException("Transfer with given details is invalid or does not exist\n" + msg);
    }
    ExceptionThrower.throwException(body.getSubCode() ,
            body.getRequestId(),
            body.getMessage());
    return  body;
  }

  public BatchTransferResponse requestBatchTransfer(BatchTransferRequest batchTransferRequest) {
    URIBuilder uriBuilder = null;
    try {
      uriBuilder =
              new URIBuilder(Payouts.getEndpoint() + PayoutConstants.REQUEST_BATCH_TRANSFER_REL_URL);
    } catch (Exception e) {
      e.printStackTrace();
    }
    BatchTransferResponse body = payouts.performPostRequest(
            uriBuilder.toString(),
        batchTransferRequest,
        BatchTransferResponse.class);
    if (200 == body.getSubCode() || 201 == body.getSubCode() || 202 == body.getSubCode()) {
      return body;
    } else if (409 == body.getSubCode()) {
      throw new ResourceAlreadyExistsException("Batch transferId alrady exists");
    } else if (412 == body.getSubCode()) {
      throw new IllegalPayloadException("Parameters missing in request");
    }
    ExceptionThrower.throwException(body.getSubCode() ,
            body.getRequestId(),
            body.getMessage());
    return  body;
  }

  public BatchTransferStatusResponse getBatchTransferStatus(String batchTransferId) {
    URIBuilder uriBuilder = null;
    try {
      uriBuilder =
              new URIBuilder(Payouts.getEndpoint() + PayoutConstants.GET_BATCH_TRANSFER_STATUS_REL_URL);
    } catch (Exception e) {
      e.printStackTrace();
    }
    uriBuilder.addParameter("batchTransferId", batchTransferId);

    BatchTransferStatusResponse body = payouts.performGetRequest(uriBuilder.toString(), BatchTransferStatusResponse.class);
    if (200 == body.getSubCode() || 201 == body.getSubCode() || 202 == body.getSubCode()) {
      return body;
    } else if (404 == body.getSubCode()) {
      throw new IllegalPayloadException("Batch Transfer Id does not exist");
    }
    ExceptionThrower.throwException(body.getSubCode() ,
            body.getRequestId(),
            body.getMessage());
    return body;
  }
}
