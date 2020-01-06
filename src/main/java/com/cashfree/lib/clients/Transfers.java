package com.cashfree.lib.clients;

import java.util.List;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.apache.commons.lang3.StringUtils;

import com.cashfree.lib.exceptions.IllegalPayloadException;
import com.cashfree.lib.exceptions.UnknownExceptionOccured;
import com.cashfree.lib.exceptions.ResourceDoesntExistException;
import com.cashfree.lib.exceptions.ResourceAlreadyExistsException;

import com.cashfree.lib.domains.TransferDetails;
import com.cashfree.lib.domains.request.BatchTransferRequest;
import com.cashfree.lib.domains.response.GetTransferResponse;
import com.cashfree.lib.domains.response.GetTransfersResponse;
import com.cashfree.lib.domains.response.BatchTransferResponse;
import com.cashfree.lib.domains.request.RequestTransferRequest;
import com.cashfree.lib.domains.response.RequestTransferResponse;
import com.cashfree.lib.domains.response.BatchTransferStatusResponse;

import com.cashfree.lib.constants.PayoutConstants;

public class Transfers {
  private Payouts payouts;

  public Transfers(Payouts payouts) {
    this.payouts = payouts;
  }

  public RequestTransferResponse.Payload requestTransfer(RequestTransferRequest transferRequest) {
    RequestTransferResponse body = payouts.performPostRequest(
        PayoutConstants.REQUEST_TRANSFER_REL_URL,
        transferRequest,
        RequestTransferResponse.class);
    if (HttpStatus.OK.value() == body.getSubCode()) {
      return body.getData();
    } else if (HttpStatus.NOT_FOUND.value() == body.getSubCode()) {
      throw new ResourceDoesntExistException("Beneficiary does not exist");
    } else if (HttpStatus.CONFLICT.value() == body.getSubCode()) {
      throw new IllegalPayloadException("Remarks can have only numbers, alphabets and whitespaces");
    }
    throw new UnknownExceptionOccured();
  }

  public TransferDetails getTransferStatus(String referenceId, String transferId) {
    if (StringUtils.isBlank(referenceId) && StringUtils.isBlank(transferId)) {
      throw new IllegalPayloadException("Either referenceId or transferId is mandatory to retrieve status of the transfer");
    }
    GetTransferResponse body = payouts.performGetRequest(
        PayoutConstants.GET_TRANSFER_STATUS_REL_URL,
        GetTransferResponse.class, transferId);
    if (HttpStatus.OK.value() == body.getSubCode()) {
      return body.getData().getTransfer();
    } else if (HttpStatus.NOT_FOUND.value() == body.getSubCode()) {
      throw new ResourceDoesntExistException("Transfer with given details is invalid or does not exist");
    }
    throw new UnknownExceptionOccured();
  }

  public List<TransferDetails> getTransfers(Integer maxReturn, Integer lastReturnId, LocalDateTime date) {
    GetTransfersResponse body = payouts.performGetRequest(
        PayoutConstants.GET_TRANSFERS_REL_URL,
        GetTransfersResponse.class, maxReturn, lastReturnId, date);
    if (HttpStatus.OK.value() == body.getSubCode()) {
      return body.getData().getTransfers();
    } else if (HttpStatus.PRECONDITION_FAILED.value() == body.getSubCode()) {
      throw new IllegalPayloadException("Invalid maxReturn value passed");
    }
    throw new UnknownExceptionOccured();
  }

  public List<RequestTransferResponse> requestBatchTransfer(BatchTransferRequest batchTransferRequest) {
    BatchTransferResponse body = payouts.performPostRequest(
        PayoutConstants.REQUEST_BATCH_TRANSFER_REL_URL,
        batchTransferRequest,
        BatchTransferResponse.class);
    if (HttpStatus.OK.value() == body.getSubCode()) {
      return body.getBatch();
    } else if (HttpStatus.CONFLICT.value() == body.getSubCode()) {
      throw new ResourceAlreadyExistsException("Batch transferId alrady exists");
    } else if (HttpStatus.PRECONDITION_FAILED.value() == body.getSubCode()) {
      throw new IllegalPayloadException("Parameters missing in request");
    }
    throw new UnknownExceptionOccured();
  }

  public List<TransferDetails> getBatchTransferStatus(String batchTransferId) {
    BatchTransferStatusResponse body = payouts.performGetRequest(
        PayoutConstants.GET_BATCH_TRANSFER_STATUS_REL_URL,
        BatchTransferStatusResponse.class, batchTransferId);
    if (HttpStatus.OK.value() == body.getSubCode()) {
      return body.getData().getTransfers();
    } else if (HttpStatus.NOT_FOUND.value() == body.getSubCode()) {
      throw new IllegalPayloadException("Batch Transfer Id does not exist");
    }
    throw new UnknownExceptionOccured();
  }
}
