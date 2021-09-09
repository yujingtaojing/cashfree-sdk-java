package com.cashfree.lib.experiments;

import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.logging.Logger;
import java.util.logging.ConsoleHandler;
import java.util.concurrent.ThreadLocalRandom;

import com.cashfree.lib.constants.Constants.Environment;
import com.cashfree.lib.exceptions.IllegalPayloadException;
import com.cashfree.lib.exceptions.ResourceDoesntExistException;
import com.cashfree.lib.exceptions.ResourceAlreadyExistsException;

import com.cashfree.lib.payout.clients.Payouts;
import com.cashfree.lib.payout.clients.Cashgram;
import com.cashfree.lib.payout.clients.Transfers;
import com.cashfree.lib.payout.clients.Validation;
import com.cashfree.lib.payout.clients.Beneficiary;

import com.cashfree.lib.payout.domains.CashgramDetails;
import com.cashfree.lib.payout.domains.BeneficiaryDetails;
import com.cashfree.lib.payout.domains.response.CfPayoutsResponse;
import com.cashfree.lib.payout.domains.response.GetBalanceResponse;
import com.cashfree.lib.payout.domains.request.BatchTransferRequest;
import com.cashfree.lib.payout.domains.request.SelfWithdrawalRequest;
import com.cashfree.lib.payout.domains.request.BulkValidationRequest;
import com.cashfree.lib.payout.domains.response.BatchTransferResponse;
import com.cashfree.lib.payout.domains.request.RequestTransferRequest;
import com.cashfree.lib.payout.domains.response.BulkValidationResponse;

import com.cashfree.lib.utils.CommonUtils;
import com.cashfree.lib.logger.VerySimpleFormatter;

public class PayoutExperiments {
  private static final Logger log = Logger.getLogger(PayoutExperiments.class.getName());
  static {
    ConsoleHandler consoleHandler = new ConsoleHandler();
    consoleHandler.setFormatter(new VerySimpleFormatter());
    log.addHandler(consoleHandler);
  }
  
  public static void main(String[] args) {

    String clientId = "CF27JBMB8GN4CHAQI6Q";
    String clientSecret = "fd48e5a6084d611e4fd9c6f0f8fcbca16d221ace";
    Environment env = Environment.TEST;
    String publicKeyPath = "";
    Payouts payouts = Payouts.getInstance(
            env,
            clientId,
            clientSecret,
            publicKeyPath );

    log.info("" + payouts.init());

    boolean isTokenValid = payouts.verifyToken();
    log.info("" + isTokenValid);

    if (!isTokenValid) {
      System.out.println("INVALID TOKEN");
      return;
    }

    testPayoutEndpoints(payouts);
    testBeneficiaryEndpoints(new Beneficiary(payouts));
    testValidationEndpoints(new Validation(payouts));
    testTransfersEndpionts(new Transfers(payouts));
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
        .setVpa("johndoe@hdfcbank")
        .setCardNo("4111111111111111")
        .setAddress1("ABC Street")
        .setCity("Bangalore")
        .setState("Karnataka")
        .setPincode("560001");

    try {
      log.info("" + beneficiary.addBeneficiary(beneficiaryDetails));
    } catch (ResourceAlreadyExistsException x) {
      log.warning(x.getMessage());
    }
    try {
      log.info("" + beneficiary.getBeneficiaryDetails("JOHN18012"));
      log.info("" + beneficiary.getBeneficiaryId("00001111222238", "HDFC0000001"));
    } catch (ResourceDoesntExistException x) {
      log.warning(x.getMessage());
    }
    try {
      log.info("" + beneficiary.removeBeneficiary("JOHN18012"));
    } catch (ResourceDoesntExistException x) {
      log.warning(x.getMessage());
    }
  }

  private static void testValidationEndpoints(Validation validation) {
    log.info("" + validation.validateBankDetails(
            "JOHN Doe", "9908712345", "026291800001191", "YESB0000262"));
    log.info("" + validation.validateUPIDetails("Shubhankar Saha", "success@upi"));


    List<BulkValidationRequest.Payload> entries = new ArrayList<>();
    entries.add(new BulkValidationRequest.Payload()
        .setName("Sameera Cashfree")
        .setBankAccount("000890289871772")
        .setIfsc("SCBL0036078")
        .setPhone("9015991882"));
    entries.add(new BulkValidationRequest.Payload()
        .setName("Shubhankar Saha")
        .setBankAccount("387312345234")
        .setIfsc("SBIN0021161")
        .setPhone("9073397707"));
    String bulkValidationId = "javasdktest" + ThreadLocalRandom.current().nextInt(0, 1000000);
    BulkValidationRequest request = new BulkValidationRequest()
        .setBulkValidationId(bulkValidationId)
        .setEntries(entries);
    BulkValidationResponse bulkValidationResponse = validation.validateBulkBankActivation(request);
    log.info("" + bulkValidationResponse);
    if (bulkValidationResponse.getData() != null) {
      String status = bulkValidationResponse.getData().getBulkValidationId();
      if (CommonUtils.isNotBlank(status)) {
        log.info("" + validation.getBulkValidationStatus(status, null, null));
      }
    }
  }

  private static void testTransfersEndpionts(Transfers transfers) {
    String transferId = "javasdktesttransferid" + ThreadLocalRandom.current().nextInt(0, 1000000);

    RequestTransferRequest request = new RequestTransferRequest()
        .setBeneId("johndoevalid983")
        .setAmount(new BigDecimal("1.00"))
        .setTransferId(transferId);
    try {
      log.info("" + transfers.requestTransfer(request));
    } catch (IllegalPayloadException x) {
      log.warning(x.getMessage());
    }
    try {
      log.info("" + transfers.getTransferStatus(null, transferId));
    } catch (ResourceDoesntExistException x) {
      log.warning(x.getMessage());
    }
//    log.info("" + transfers.getTransfers(10, null, null));    //provided for limited merchants

    String batchTransferId = "javasdktestbatchtransferid" + ThreadLocalRandom.current().nextInt(0, 1000000);
    List<BatchTransferRequest.Payload> batchTransferRequests = new ArrayList<>();
    batchTransferRequests.add(
        new BatchTransferRequest.Payload()
            .setTransferId("PTM_00121241112")
            .setAmount(new BigDecimal("1.00"))
            .setPhone("9999999999")
            .setBankAccount("9999999999")
            .setIfsc("PYTM0_000001")
            .setEmail("bahrat@gocashfree.com")
            .setName("bharat"));
    batchTransferRequests.add(
        new BatchTransferRequest.Payload()
            .setTransferId("PTM_00052312126")
            .setAmount(new BigDecimal("1.00"))
            .setPhone("9999999999")
            .setBankAccount("9999999999")
            .setIfsc("PYTM0000001")
            .setEmail("bharat@gocashfree.com")
            .setName("bharat"));
    batchTransferRequests.add(
        new BatchTransferRequest.Payload()
            .setTransferId("PTM_0001321215")
            .setAmount(new BigDecimal("1.00"))
            .setPhone("9999999999")
            .setBankAccount("9999999999")
            .setIfsc("PYTM0000001")
            .setEmail("bahrat@gocashfree.com")
            .setName("bharat"));
    BatchTransferRequest batchTransferRequest = new BatchTransferRequest()
        .setBatchTransferId(batchTransferId)
        .setBatchFormat("BANK_ACCOUNT")
        .setDeleteBene(1)
        .setBatch(batchTransferRequests);

    BatchTransferResponse batchTransferResponse = transfers.requestBatchTransfer(batchTransferRequest);
    log.info("" + batchTransferResponse);
    log.info("" + transfers.getBatchTransferStatus(batchTransferId));
  }


  private static void testCashgramEndpoints(Cashgram cashgram) {
    String cashgramId = "javasdktestcashgram" + ThreadLocalRandom.current().nextInt(0, 1000000);

    CashgramDetails cashgramDetails = new CashgramDetails()
        .setCashgramId(cashgramId)
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
      log.warning(x.getMessage());
    }
    log.info("" + cashgram.getCashgramStatus(cashgramId));
    log.info("" + cashgram.deactivateCashgram(cashgramId));
  }

  private static void testPayoutEndpoints(Payouts payouts) {
    payouts.init();
    GetBalanceResponse ledgerDetails = payouts.getBalance();
    log.info("" + ledgerDetails);

    String withdrawalId = "javasdktestwithdrawal" + ThreadLocalRandom.current().nextInt(0, 1000000);
    SelfWithdrawalRequest selfWithdrawalRequest = new SelfWithdrawalRequest()
        .setWithdrawalId(withdrawalId)
        .setAmount(new BigDecimal("2"))
        .setRemarks("Sample Withdrawal Request");
    CfPayoutsResponse withdrawalResponse = payouts.selfWithdrawal(selfWithdrawalRequest);
    log.info("" + withdrawalResponse);
  }
}
