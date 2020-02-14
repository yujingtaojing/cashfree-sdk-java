package com.cashfree.lib.experiments;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.logging.Logger;
import java.util.logging.ConsoleHandler;
import java.util.concurrent.ThreadLocalRandom;

import com.cashfree.lib.constants.Constants;
import com.cashfree.lib.logger.VerySimpleFormatter;

import com.cashfree.lib.pg.clients.Pg;
import com.cashfree.lib.pg.clients.Order;
import com.cashfree.lib.pg.clients.Refunds;
import com.cashfree.lib.pg.clients.Settlements;
import com.cashfree.lib.pg.clients.Transactions;

import com.cashfree.lib.pg.domains.request.*;
import com.cashfree.lib.pg.domains.response.*;

public class PgExperiments {
  private static final Logger log = Logger.getLogger(PayoutExperiments.class.getName());
  static {
    ConsoleHandler consoleHandler = new ConsoleHandler();
    consoleHandler.setFormatter(new VerySimpleFormatter());
    log.addHandler(consoleHandler);
  }

  public static void main(String[] args) {
    Pg pg = Pg.getInstance(
        Constants.Environment.PRODUCTION, "1848d0ce8441fb8ffa258bc98481", "62f1476aee1c57c7bef6259e104f9a868b068ed6");

    log.info("Verifying Credentials: " + pg.verifyCredentials());

    Order order = new Order(pg);
    Transactions transactions = new Transactions(pg);
    Refunds refunds = new Refunds(pg);
    Settlements settlements = new Settlements(pg);

    verifyOrderEndpoints(order);
    verifyTransactionsEndpoints(transactions);
    verifyRefundsEndpoints(refunds);
    verifySettlementsEndpoints(settlements);
  }

  private static void verifyOrderEndpoints(Order order) {
    String orderId = "javasdktestordercreation" + ThreadLocalRandom.current().nextInt(0, 1000000);
    CreateOrderRequest createOrderRequest =
        new CreateOrderRequest()
            .setOrderId(orderId)
            .setOrderAmount(new BigDecimal("1.00"))
            .setOrderNote("Test order creation for javasdk.")
            .setCustomerName("Sameera")
            .setCustomerPhone("9085433894").setCustomerEmail("ranu01@cashfree.com")
            .setReturnUrl("http://yo.com");

    CreateOrderResponse createOrderResponse = order.createOrder(createOrderRequest);
    log.info("Order Creation Response: " + createOrderResponse);

    GetOrderLinkResponse getLinkResponse = order.getLinkForOrder(orderId);
    log.info("For orderId: " + orderId + " fetching link, response: " + getLinkResponse);

    GetOrderDetailsResponse getOrderDetailsResponse = order.getDetailsForOrder(orderId);
    log.info("For orderId: " + orderId + " details are: " + getOrderDetailsResponse);

    GetOrderStatusResponse getOrderStatusResponse = order.getStatusForOrder(orderId);
    log.info("For orderId: " + orderId + " status is: " + getOrderStatusResponse);

    TriggerPaymentEmailResponse orderEmailResponse = order.triggerPaymentEmailResponse(orderId);
    log.info("For orderId: " + orderId + " orderEmailResponse is: " + orderEmailResponse);
  }

  private static void verifyTransactionsEndpoints(Transactions transactions) {
    // 2018-01-01&endDate=2018-01-11&txStatus=SUCCESS&lastId=&count=
    ListTransactionsRequest listTransactionsRequest =
        new ListTransactionsRequest()
            .setStartDate(LocalDateTime.of(2018, 1, 1, 1, 1))
            .setEndDate(LocalDateTime.of(2018, 1, 11, 1, 1))
            .setTxStatus("SUCCESS");
    ListTransactionsResponse listTransactionsResponse = transactions.getTransactions(listTransactionsRequest);
    log.info("For request: " + listTransactionsRequest + " listTransactionsResponse is: " + listTransactionsResponse);
  }

  private static void verifyRefundsEndpoints(Refunds refunds) {
    InitiateRefundRequest initiateRefundRequest =
        new InitiateRefundRequest()
            .setReferenceId("13307")
            .setRefundAmount(new BigDecimal("102.00"))
            .setRefundNote("Sample Refund");
    TriggerOrderRefundResponse refundResponse = refunds.triggerOrderRefund(initiateRefundRequest);
    log.info("For request: " + initiateRefundRequest + " orderRefundResponse is: " + refundResponse);

    refundResponse = refunds.triggerInstantOrderRefund(initiateRefundRequest);
    log.info("For request: " + initiateRefundRequest + " instant refund response is: " + refundResponse);

    ListRefundsRequest listRefundsRequest =
        new ListRefundsRequest()
            .setStartDate(LocalDateTime.of(2018, 1, 1, 1, 1))
            .setEndDate(LocalDateTime.of(2018, 1, 15, 1, 1));
    ListRefundsResponse refundsResponse = refunds.getRefundsForMerchant(listRefundsRequest);
    log.info("For request: " + listRefundsRequest + " refunds response is: " + refundsResponse);

    GetRefundResponse response = refunds.getRefund("123", null);
    log.info("For refundId: 123 refunds response is: " + response);
  }

  private static void verifySettlementsEndpoints(Settlements settlements) {
    ListSettlementsRequest listSettlementsRequest =
        new ListSettlementsRequest()
            .setStartDate(LocalDateTime.of(2018, 1, 1, 1, 1))
            .setEndDate(LocalDateTime.of(2018, 1, 15, 1, 1));

    ListSettlementsResponse listSettlementsResponse = settlements.fetchAllSettlements(listSettlementsRequest);
    log.info("For request: " + listSettlementsRequest + " settlements response is: " + listSettlementsResponse);

    FetchSettlementResponse fetchSettlementResponse = settlements.fetchSettlement("123456", null, null);
    log.info("For settlementId: 123456 settlements response is: " + fetchSettlementResponse);
  }
}
