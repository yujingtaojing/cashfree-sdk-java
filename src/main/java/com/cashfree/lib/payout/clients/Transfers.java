package com.cashfree.lib.payout.clients;

import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.cashfree.lib.exceptions.IllegalPayloadException;
import com.cashfree.lib.exceptions.UnknownExceptionOccured;
import com.cashfree.lib.exceptions.ResourceDoesntExistException;
import com.cashfree.lib.exceptions.ResourceAlreadyExistsException;

import com.cashfree.lib.payout.domains.TransferDetails;
import com.cashfree.lib.payout.domains.request.BatchTransferRequest;
import com.cashfree.lib.payout.domains.response.GetTransferResponse;
import com.cashfree.lib.payout.domains.response.GetTransfersResponse;
import com.cashfree.lib.payout.domains.response.BatchTransferResponse;
import com.cashfree.lib.payout.domains.request.RequestTransferRequest;
import com.cashfree.lib.payout.domains.response.RequestTransferResponse;
import com.cashfree.lib.payout.domains.response.BatchTransferStatusResponse;

import com.cashfree.lib.http.UriBuilder;
import com.cashfree.lib.utils.CommonUtils;
import com.cashfree.lib.payout.constants.PayoutConstants;

public class Transfers {
  private Payouts payouts;

  public Transfers(Payouts payouts) {
    this.payouts = payouts;
  }

  public RequestTransferResponse requestTransfer(RequestTransferRequest transferRequest) {
    RequestTransferResponse body = payouts.performPostRequest(
        PayoutConstants.REQUEST_TRANSFER_REL_URL,
        transferRequest,
        RequestTransferResponse.class);
    if (200 == body.getSubCode()) {
      return body;
    } else if (201 == body.getSubCode()) {
      return body;
    } else if (404 == body.getSubCode()) {
      throw new ResourceDoesntExistException("Beneficiary does not exist");
    } else if (409 == body.getSubCode()) {
      throw new IllegalPayloadException(body.getMessage());
    } else if (412 == body.getSubCode()) {
      throw new IllegalPayloadException(body.getMessage());
    }
    throw new UnknownExceptionOccured(body.getMessage());
  }

  public GetTransferResponse getTransferStatus(String referenceId, String transferId) {
    if (CommonUtils.isBlank(referenceId) && CommonUtils.isBlank(transferId)) {
      throw new IllegalPayloadException("Either referenceId or transferId is mandatory to retrieve status of the transfer");
    }

    UriBuilder uri = UriBuilder.fromUriString(PayoutConstants.GET_TRANSFER_STATUS_REL_URL);
    if (CommonUtils.isNotBlank(transferId)) {
      uri.queryParam("transferId", transferId);
    }
    if (CommonUtils.isNotBlank(referenceId)) {
      uri.queryParam("referenceId", referenceId);
    }
    GetTransferResponse body = payouts.performGetRequest(uri.toUriString(), GetTransferResponse.class);
    if (200 == body.getSubCode()) {
      return body;
    } else if (404 == body.getSubCode()) {
      throw new ResourceDoesntExistException("Transfer with given details is invalid or does not exist");
    }
    throw new UnknownExceptionOccured(body.getMessage());
  }

  public GetTransfersResponse getTransfers(Integer maxReturn, Integer lastReturnId, LocalDateTime date) {
    UriBuilder uri = UriBuilder.fromUriString(PayoutConstants.GET_TRANSFERS_REL_URL);
    if (maxReturn != null) {
      uri.queryParam("maxReturn", maxReturn.toString());
    }
    if (lastReturnId != null) {
      uri.queryParam("lastReturnId", lastReturnId.toString());
    }
    if (date != null) {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

      uri.queryParam("date", formatter.format(date));
    }
    return getTransfers(uri.toUriString());
  }

  public GetTransfersResponse getTransfers(String relUrl) {
    GetTransfersResponse body = payouts.performGetRequest(relUrl, GetTransfersResponse.class);
    if (200 == body.getSubCode()) {
      return body;
    } else if (412 == body.getSubCode()) {
      throw new IllegalPayloadException("Invalid maxReturn value passed");
    }
    throw new UnknownExceptionOccured(body.getMessage());
  }

  public BatchTransferResponse requestBatchTransfer(BatchTransferRequest batchTransferRequest) {
    BatchTransferResponse body = payouts.performPostRequest(
        PayoutConstants.REQUEST_BATCH_TRANSFER_REL_URL,
        batchTransferRequest,
        BatchTransferResponse.class);
    if (200 == body.getSubCode()) {
      return body;
    } else if (409 == body.getSubCode()) {
      throw new ResourceAlreadyExistsException("Batch transferId alrady exists");
    } else if (412 == body.getSubCode()) {
      throw new IllegalPayloadException("Parameters missing in request");
    }
    throw new UnknownExceptionOccured(body.getMessage());
  }

  public BatchTransferStatusResponse getBatchTransferStatus(String batchTransferId) {
    UriBuilder uri = UriBuilder.fromUriString(PayoutConstants.GET_BATCH_TRANSFER_STATUS_REL_URL)
        .queryParam("batchTransferId", batchTransferId);

    BatchTransferStatusResponse body = payouts.performGetRequest(uri.toUriString(), BatchTransferStatusResponse.class);
    if (200 == body.getSubCode()) {
      return body;
    } else if (404 == body.getSubCode()) {
      throw new IllegalPayloadException("Batch Transfer Id does not exist");
    }
    throw new UnknownExceptionOccured(body.getMessage());
  }
}
