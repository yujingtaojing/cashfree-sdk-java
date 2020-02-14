package com.cashfree.lib.payout.clients;

import java.util.*;

import com.cashfree.lib.payout.domains.response.CfPayoutsResponse;
import com.cashfree.lib.payout.domains.response.GetBalanceResponse;
import com.cashfree.lib.payout.domains.request.SelfWithdrawalRequest;
import com.cashfree.lib.payout.domains.response.AuthenticationResponse;

import com.cashfree.lib.payout.constants.Endpoints;
import com.cashfree.lib.payout.constants.PayoutConstants;
import com.cashfree.lib.constants.Constants.Environment;

import com.cashfree.lib.exceptions.UnknownExceptionOccured;
import com.cashfree.lib.exceptions.InvalidCredentialsException;

import com.cashfree.lib.http.HttpUtils;
import com.cashfree.lib.payout.domains.response.SelfWithdrawalResponse;

public class Payouts {

  private String clientId;
  private String clientSecret;

  private String endpoint;
  private String bearerToken;

  private static Payouts SINGLETON_INSTANCE;

  private Payouts(Environment env, String clientId, String clientSecret) {
    this.clientId = clientId;
    this.clientSecret = clientSecret;

    if (Environment.PRODUCTION.equals(env)) {
      this.endpoint = Endpoints.PROD_ENDPOINT;
    } else if (Environment.TEST.equals(env)) {
      this.endpoint = Endpoints.TEST_ENDPOINT;
    }
  }

  public static Payouts getInstance(Environment env, String clientId, String clientSecret) {
    if (SINGLETON_INSTANCE == null) {
      SINGLETON_INSTANCE = new Payouts(env, clientId, clientSecret);
    }
    return SINGLETON_INSTANCE;
  }

  public boolean init() {
    try {
      updateBearerToken();
      return true;
    } catch (Exception x) {
      return false;
    }
  }

  private Map<String, String> buildAuthHeader() {
    Map<String, String> headers = new HashMap<>();
    headers.put("Authorization", "Bearer " + bearerToken);
    return headers;
  }

  void updateBearerToken() {
    // Setup headers.
    Map<String, String> headersMap = new HashMap<>();
    headersMap.put("X-Client-Id", clientId);
    headersMap.put("X-Client-Secret", clientSecret);

    AuthenticationResponse body =
        HttpUtils.performPostRequest(
            endpoint + PayoutConstants.AUTH_REL_URL, headersMap, null, AuthenticationResponse.class);

    if (body == null) {
      throw new UnknownExceptionOccured();
    }
    if (200 == body.getSubCode()) {
      if (body.getData() == null) {
        throw new UnknownExceptionOccured();
      }
      bearerToken = body.getData().getToken();
    } else if (401 == body.getSubCode()) {
      throw new InvalidCredentialsException();
    }
  }

  public boolean verifyToken() {
    Map<String, String> authHeaders = buildAuthHeader();
    CfPayoutsResponse body =
        HttpUtils.performPostRequest(
            endpoint + PayoutConstants.VERIFY_TOKEN_REL_URL, authHeaders, null, CfPayoutsResponse.class);

    if (body == null) {
      throw new UnknownExceptionOccured();
    }
    if (200 == body.getSubCode()) {
      return true;
    } else if (403 == body.getSubCode()) {
      return false;
    }
    return false;
  }

  <Request, Response extends CfPayoutsResponse> Response
  performPostRequest(String relUrl, Request request, Class<Response> clazz) {
    Map<String, String> authHeaders = buildAuthHeader();

    Response body =
        HttpUtils.performPostRequest(endpoint + relUrl, authHeaders, request, clazz);

    if (body == null) {
      throw new UnknownExceptionOccured();
    }
    // TODO Return this as it is. Make change in the core api itself instead of adding a fix.
    if (body.getSubCode() == null) {
      return body;
    }
    if (403 == body.getSubCode()) {
      updateBearerToken();
      performPostRequest(relUrl, request, clazz);
    }
    return body;
  }

  <Response extends CfPayoutsResponse> Response performGetRequest(String relUrl, Class<Response> clazz) {
    Map<String, String> authHeaders = buildAuthHeader();
    Response body =
        HttpUtils.performGetRequest(endpoint + relUrl, authHeaders, clazz);
    if (body == null) {
      throw new UnknownExceptionOccured();
    }
    if (403 == body.getSubCode()) {
      updateBearerToken();
      performGetRequest(relUrl, clazz);
    }
    return body;
  }

  public GetBalanceResponse getBalance() {
    GetBalanceResponse body = performGetRequest(
        PayoutConstants.GET_BALANCE_REL_URL, GetBalanceResponse.class);
    if (200 == body.getSubCode()) {
      return body;
    }
    throw new UnknownExceptionOccured("Unable to fetch beneficiary id");
  }

  public SelfWithdrawalResponse selfWithdrawal(SelfWithdrawalRequest selfWithdrawalRequest) {
    SelfWithdrawalResponse body = performPostRequest(
        PayoutConstants.SELF_WITHDRAWAL_REL_URL, selfWithdrawalRequest, SelfWithdrawalResponse.class);

    if (200 == body.getStatusCode() || 400 == body.getStatusCode()) {
      return body;
    }
    throw new UnknownExceptionOccured(body.getMessage());
  }
}
