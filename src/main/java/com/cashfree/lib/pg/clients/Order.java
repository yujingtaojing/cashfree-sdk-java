package com.cashfree.lib.pg.clients;

import java.util.Map;
import java.util.HashMap;

import com.cashfree.lib.utils.CommonUtils;

import com.cashfree.lib.pg.domains.response.*;
import com.cashfree.lib.pg.constants.PgConstants;
import com.cashfree.lib.pg.domains.request.CreateOrderRequest;

public class Order {
  private Pg pg;

  public Order(Pg pg) {
    this.pg = pg;
  }

  public CreateOrderResponse createOrder(CreateOrderRequest request) {
    return pg.performPostRequest(
        PgConstants.ORDER_CREATE_REL_URL, request, CreateOrderRequest.class, CreateOrderResponse.class);
  }

  public GetOrderLinkResponse getLinkForOrder(String orderId) {
    if (CommonUtils.isBlank(orderId)) {
      throw new IllegalArgumentException("OrderId can't be blank or null.");
    }
    Map<String, String> map = new HashMap<>();
    map.put("orderId", orderId);
    return pg.performPostRequest(PgConstants.ORDER_GET_LINK_REL_URL, map, GetOrderLinkResponse.class);
  }

  public GetOrderDetailsResponse getDetailsForOrder(String orderId) {
    if (CommonUtils.isBlank(orderId)) {
      throw new IllegalArgumentException("OrderId can't be blank or null.");
    }
    Map<String, String> map = new HashMap<>();
    map.put("orderId", orderId);
    return pg.performPostRequest(PgConstants.ORDER_GET_DETAILS_REL_URL, map, GetOrderDetailsResponse.class);
  }

  public GetOrderStatusResponse getStatusForOrder(String orderId) {
    if (CommonUtils.isBlank(orderId)) {
      throw new IllegalArgumentException("OrderId can't be blank or null.");
    }
    Map<String, String> map = new HashMap<>();
    map.put("orderId", orderId);
    return pg.performPostRequest(PgConstants.ORDER_GET_STATUS_REL_URL, map, GetOrderStatusResponse.class);
  }

  public TriggerPaymentEmailResponse triggerPaymentEmailResponse(String orderId) {
    if (CommonUtils.isBlank(orderId)) {
      throw new IllegalArgumentException("OrderId can't be blank or null.");
    }
    Map<String, String> map = new HashMap<>();
    map.put("orderId", orderId);
    return pg.performPostRequest(PgConstants.ORDER_EMAIL_REL_URL, map, TriggerPaymentEmailResponse.class);
  }
}
