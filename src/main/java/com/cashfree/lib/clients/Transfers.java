package com.cashfree.lib.clients;

import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.http.HttpStatus;
import org.springframework.web.util.UriComponentsBuilder;

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
    } else if (HttpStatus.CREATED.value() == body.getSubCode()) {
      return body.getData();
    } else if (HttpStatus.NOT_FOUND.value() == body.getSubCode()) {
      throw new ResourceDoesntExistException("Beneficiary does not exist");
    } else if (HttpStatus.CONFLICT.value() == body.getSubCode()) {
      throw new IllegalPayloadException(body.getMessage());
    } else if (HttpStatus.PRECONDITION_FAILED.value() == body.getSubCode()) {
      throw new IllegalPayloadException(body.getMessage());
    }
    throw new UnknownExceptionOccured();
  }

  public TransferDetails getTransferStatus(String referenceId, String transferId) {
    if (StringUtils.isBlank(referenceId) && StringUtils.isBlank(transferId)) {
      throw new IllegalPayloadException("Either referenceId or transferId is mandatory to retrieve status of the transfer");
    }

    UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(PayoutConstants.GET_TRANSFER_STATUS_REL_URL);
    if (StringUtils.isNotBlank(transferId)) {
      uri.queryParam("transferId", transferId);
    }
    if (StringUtils.isNotBlank(referenceId)) {
      uri.queryParam("referenceId", referenceId);
    }
    GetTransferResponse body = payouts.performGetRequest(uri.toUriString(), GetTransferResponse.class);
    if (HttpStatus.OK.value() == body.getSubCode()) {
      return body.getData().getTransfer();
    } else if (HttpStatus.NOT_FOUND.value() == body.getSubCode()) {
      throw new ResourceDoesntExistException("Transfer with given details is invalid or does not exist");
    }
    throw new UnknownExceptionOccured();
  }

  public List<TransferDetails> getTransfers(Integer maxReturn, Integer lastReturnId, LocalDateTime date) {
    UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(PayoutConstants.GET_TRANSFERS_REL_URL);
    if (maxReturn != null) {
      uri.queryParam("maxReturn", maxReturn);
    }
    if (lastReturnId == null) {
      uri.queryParam("lastReturnId", lastReturnId);
    }
    if (date != null) {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

      uri.queryParam("date", formatter.format(date));
    }

    return getTransfers(uri.toUriString());
  }

  public List<TransferDetails> getTransfers(String relUrl) {
    GetTransfersResponse body = payouts.performGetRequest(relUrl, GetTransfersResponse.class);
    if (HttpStatus.OK.value() == body.getSubCode()) {
      return body.getData().getTransfers();
    } else if (HttpStatus.PRECONDITION_FAILED.value() == body.getSubCode()) {
      throw new IllegalPayloadException("Invalid maxReturn value passed");
    }
    throw new UnknownExceptionOccured();
  }

  public BatchTransferResponse.Payload requestBatchTransfer(BatchTransferRequest batchTransferRequest) {
    BatchTransferResponse body = payouts.performPostRequest(
        PayoutConstants.REQUEST_BATCH_TRANSFER_REL_URL,
        batchTransferRequest,
        BatchTransferResponse.class);
    if (HttpStatus.OK.value() == body.getSubCode()) {
      return body.getData();
    } else if (HttpStatus.CONFLICT.value() == body.getSubCode()) {
      throw new ResourceAlreadyExistsException("Batch transferId alrady exists");
    } else if (HttpStatus.PRECONDITION_FAILED.value() == body.getSubCode()) {
      throw new IllegalPayloadException("Parameters missing in request");
    }
    throw new UnknownExceptionOccured();
  }

  public List<BatchTransferStatusResponse.Payload.Transfer> getBatchTransferStatus(String batchTransferId) {
    UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(PayoutConstants.GET_BATCH_TRANSFER_STATUS_REL_URL)
        .queryParam("batchTransferId", batchTransferId);

    BatchTransferStatusResponse body = payouts.performGetRequest(uri.toUriString(), BatchTransferStatusResponse.class);
    if (HttpStatus.OK.value() == body.getSubCode()) {
      return body.getData().getTransfers();
    } else if (HttpStatus.NOT_FOUND.value() == body.getSubCode()) {
      throw new IllegalPayloadException("Batch Transfer Id does not exist");
    }
    throw new UnknownExceptionOccured();
  }
}
