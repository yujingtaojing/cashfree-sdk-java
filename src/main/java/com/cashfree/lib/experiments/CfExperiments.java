package com.cashfree.lib.experiments;

import java.util.List;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.extern.slf4j.Slf4j;

import com.cashfree.lib.clients.*;
import com.cashfree.lib.domains.CashgramDetails;
import com.cashfree.lib.domains.BeneficiaryDetails;
import com.cashfree.lib.constants.Constants.Environment;
import com.cashfree.lib.exceptions.IllegalPayloadException;
import com.cashfree.lib.domains.request.BatchTransferRequest;
import com.cashfree.lib.domains.request.BulkValidationRequest;
import com.cashfree.lib.domains.request.RequestTransferRequest;
import com.cashfree.lib.exceptions.ResourceDoesntExistException;
import com.cashfree.lib.exceptions.ResourceAlreadyExistsException;

@Slf4j
public class CfExperiments {
  public static void main(String[] args) throws IOException {
    Payouts payouts = new Payouts(
        Environment.PRODUCTION, "CF1848EZPSGLHWP9IUE2Y", "b8df7784dd3f38911294d3597764dd43f3016a48");

    payouts.updateBearerToken();

    boolean isTokenValid = payouts.verifyToken();
    log.info("" + isTokenValid);
    if (!isTokenValid) return;

    // testBeneficiaryEndpoints(new Beneficiary(payouts));
    // testValidationEndpoints(new Validation(payouts));
    // testTransfersEndpionts(new Transfers(payouts));
    testCashgramEndpoints(new Cashgram(payouts));
  }

  private static void testBeneficiaryEndpoints(Beneficiary beneficiary) {
    BeneficiaryDetails beneficiaryDetails = new BeneficiaryDetails()
        .setBeneId("JOHN18012")
        .setName("john doe")
        .setEmail("johndoe@cashfree.com")
        .setPhone("9876543210")
        .setBankAccount("00001111222233")
        .setIfsc("HDFC0000001")
        .setAddress1("ABC Street")
        .setCity("Bangalore")
        .setState("Karnataka")
        .setPincode("560001");

    try {
      log.info("" + beneficiary.addBeneficiary(beneficiaryDetails));
    } catch (ResourceAlreadyExistsException x) {
      log.warn(x.getMessage());
    }
    try {
      log.info("" + beneficiary.getBeneficiaryDetails("JOHN18012"));
      log.info("" + beneficiary.getBeneficiaryId("00001111222233", "HDFC0000001"));
    } catch (ResourceDoesntExistException x) {
      log.warn(x.getMessage());
    }
    try {
      log.info("" + beneficiary.removeBeneficiary("JOHN18012"));
    } catch (ResourceDoesntExistException x) {
      log.warn(x.getMessage());
    }
  }

  private static void testValidationEndpoints(Validation validation) {
    log.info("" + validation.validateBankDetails(
        "JOHN", "9908712345", "026291800001191", "YESB0000262"));
    log.info("" + validation.validateUPIDetails("Cashfree", "success@upi"));

    BulkValidationRequest request = new BulkValidationRequest()
        .setBulkValidationId("javasdktest1")
        .setEntries(new BulkValidationRequest.Payload[]{
            new BulkValidationRequest.Payload()
                .setName("Sameera Cashfree")
                .setBankAccount("000890289871772")
                .setIfsc("SCBL0036078")
                .setPhone("9015991882"),
            new BulkValidationRequest.Payload()
                .setName("Cashfree Sameera")
                .setBankAccount("0001001289877623")
                .setIfsc("SBIN0008752")
                .setPhone("9023991882")});
    String status = validation.validateBulkBankActivation(request);
    log.info("" + validation.getBulkValidationStatus(status, null, null));
  }

  private static void testTransfersEndpionts(Transfers transfers) {
    RequestTransferRequest request = new RequestTransferRequest()
        .setBeneId("JOHN18012")
        .setAmount(new BigDecimal("1.00"))
        .setTransferId("javasdktestJAN2019");
    try {
      // log.info("" + transfers.requestTransfer(request));
    } catch (IllegalPayloadException x) {
      log.warn(x.getMessage());
    }
    try {
      log.info("" + transfers.getTransferStatus("83345068", null));
    } catch (ResourceDoesntExistException x) {
      log.warn(x.getMessage());
    }
    log.info("" + transfers.getTransfers(10, null, null));

    BatchTransferRequest batchTransferRequest = new BatchTransferRequest()
        .setBatchTransferId("javasdkbatch12")
        .setBatchFormat("BANK_ACCOUNT")
        .setDeleteBene(1)
        .setBatch(List.of(
            new BatchTransferRequest.Payload()
                .setTransferId("PTM_00121241112")
                .setAmount(new BigDecimal("1.00"))
                .setPhone("9999999999")
                .setBankAccount("9999999999")
                .setIfsc("PYTM0_000001")
                .setEmail("bahrat@gocashfree.com")
                .setName("bharat"),
            new BatchTransferRequest.Payload()
                .setTransferId("PTM_00052312126")
                .setAmount(new BigDecimal("1.00"))
                .setPhone("9999999999")
                .setBankAccount("9999999999")
                .setIfsc("PYTM0000001")
                .setEmail("bharat@gocashfree.com")
                .setName("bharat"),
            new BatchTransferRequest.Payload()
                .setTransferId("PTM_0001321215")
                .setAmount(new BigDecimal("1.00"))
                .setPhone("9999999999")
                .setBankAccount("9999999999")
                .setIfsc("PYTM0000001")
                .setEmail("bahrat@gocashfree.com")
                .setName("bharat")));

    log.info("" + transfers.requestBatchTransfer(batchTransferRequest));
    log.info("" + transfers.getBatchTransferStatus("javasdkbatch12"));
  }

  private static void testCashgramEndpoints(Cashgram cashgram) {
    CashgramDetails cashgramDetails = new CashgramDetails()
        .setCashgramId("javasdk-test2")
        .setAmount(new BigDecimal("1.00"))
        .setName("John Doe")
        .setEmail("johndoe@cashfree.com")
        .setPhone("9876543210")
        .setLinkExpiry(LocalDateTime.now().plusMinutes(0))
        .setRemarks("api")
        .setNotifyCustomer(1);
    try {
      log.info("" + cashgram.createCashgram(cashgramDetails));
    } catch (IllegalPayloadException | ResourceAlreadyExistsException x) {
      log.warn(x.getMessage());
    }
    log.info("" + cashgram.getCashgramStatus("javasdk-test2"));
    log.info("" + cashgram.deactivateCashgram("javasdk-test2"));
  }
}
