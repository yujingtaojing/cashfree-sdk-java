package com.cashfree.lib.clients;

import java.time.Duration;
import java.io.IOException;
import java.io.InputStream;

import com.cashfree.lib.domains.request.SelfWithdrawalRequest;
import org.yaml.snakeyaml.Yaml;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;

import com.cashfree.lib.exceptions.UnknownExceptionOccured;
import com.cashfree.lib.exceptions.InvalidCredentialsException;

import com.cashfree.lib.domains.response.CfPayoutsResponse;
import com.cashfree.lib.domains.response.GetBalanceResponse;
import com.cashfree.lib.domains.response.AuthenticatonResponse;

import com.cashfree.lib.config.ConfigParams;
import com.cashfree.lib.constants.PayoutConstants;
import com.cashfree.lib.constants.Constants.Environment;

@Slf4j
public class Payouts {
  private String clientId;
  private String clientSecret;

  private RestTemplate restTemplate;

  private ConfigParams conf;

  private String endpoint;
  private String bearerToken;

  private boolean isInitialized = false;
  private static Payouts SINGLETON_INSTANCE;

  private Payouts() {
    RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
    this.restTemplate = restTemplateBuilder.setConnectTimeout(Duration.ofSeconds(10)).build();
  }

  private Payouts(Environment env, String clientId, String clientSecret) throws IOException {
    this();

    this.clientId = clientId;
    this.clientSecret = clientSecret;

    InputStream is = this.getClass().getClassLoader().getResourceAsStream("payout-config.yaml");
    if (is == null) {
      throw new IllegalArgumentException("");
    }
    loadConfig(is);
    is.close();

    if (Environment.PRODUCTION.equals(env)) {
      this.endpoint = conf.getProdEndpoint();
    } else if (Environment.TEST.equals(env)) {
      this.endpoint = conf.getTestEndpoint();
    }
  }

  public static Payouts getInstance(Environment env, String clientId, String clientSecret) throws IOException {
    if (SINGLETON_INSTANCE == null) {
      SINGLETON_INSTANCE = new Payouts(env, clientId, clientSecret);
    }
    return SINGLETON_INSTANCE;
  }

  public boolean init() {
    try {
      updateBearerToken();
      isInitialized = true;
      return true;
    } catch (Exception x) {
      return false;
    }
  }

  private void loadConfig(InputStream is) {
    Yaml yaml = new Yaml();
    this.conf = yaml.loadAs(is, ConfigParams.class);
  }

  private HttpHeaders buildAuthHeader() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    headers.set("Authorization", "Bearer " + bearerToken);

    return headers;
  }

  void updateBearerToken() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    headers.set("X-Client-Id", clientId);
    headers.set("X-Client-Secret", clientSecret);

    HttpEntity<Void> httpEntity = new HttpEntity<>(headers);
    ResponseEntity<AuthenticatonResponse> response =
        restTemplate.exchange(
            endpoint + PayoutConstants.AUTH_REL_URL,
            HttpMethod.POST,
            httpEntity,
            AuthenticatonResponse.class);

    AuthenticatonResponse body = response.getBody();
    if (body == null) {
      throw new UnknownExceptionOccured();
    }
    if (HttpStatus.OK.value() == body.getSubCode()) {
      if (body.getData() == null) {
        throw new UnknownExceptionOccured();
      }
      bearerToken = body.getData().getToken();
    } else if (HttpStatus.UNAUTHORIZED.value() == body.getSubCode()) {
      throw new InvalidCredentialsException();
    }
  }

  public boolean verifyToken() {
    HttpEntity<Void> httpEntity = new HttpEntity<>(buildAuthHeader());
    ResponseEntity<CfPayoutsResponse> response =
        restTemplate.exchange(
            endpoint + PayoutConstants.VERIFY_TOKEN_REL_URL,
            HttpMethod.POST,
            httpEntity,
            CfPayoutsResponse.class);

    CfPayoutsResponse body = response.getBody();
    if (body == null) {
      throw new UnknownExceptionOccured();
    }
    if (HttpStatus.OK.value() == body.getSubCode()) {
      return true;
    } else if (HttpStatus.FORBIDDEN.value() == body.getSubCode()) {
      return false;
    }
    return false;
  }

  <Request, Response extends CfPayoutsResponse> Response
  performPostRequest(String relUrl, Request request, Class<Response> clazz) {
    HttpEntity<Request> httpEntity = new HttpEntity<>(request, buildAuthHeader());
    ResponseEntity<Response> response =
        restTemplate.exchange(endpoint + relUrl, HttpMethod.POST, httpEntity, clazz);

    Response body = response.getBody();
    if (body == null) {
      throw new UnknownExceptionOccured();
    }
    if (HttpStatus.FORBIDDEN.value() == body.getSubCode()) {
      updateBearerToken();
      performPostRequest(relUrl, request, clazz);
    }
    return body;
  }

  <Response extends CfPayoutsResponse> Response performGetRequest(String relUrl, Class<Response> clazz) {
    HttpEntity<Void> httpEntity = new HttpEntity<>(buildAuthHeader());
    ResponseEntity<Response> response =
        restTemplate.exchange(endpoint + relUrl, HttpMethod.GET, httpEntity, clazz);

    Response body = response.getBody();
    if (body == null) {
      throw new UnknownExceptionOccured();
    }
    if (HttpStatus.FORBIDDEN.value() == body.getSubCode()) {
      updateBearerToken();
      performGetRequest(relUrl, clazz);
    }
    return body;
  }

  public GetBalanceResponse.LedgerDetails getBalance() {
    GetBalanceResponse body = performGetRequest(
        PayoutConstants.GET_BALANCE_REL_URL, GetBalanceResponse.class);
    if (HttpStatus.OK.value() == body.getSubCode()) {
      return body.getData();
    }
    throw new UnknownExceptionOccured("Unable to fetch beneficiary id");
  }

  public CfPayoutsResponse selfWithdrawal(SelfWithdrawalRequest selfWithdrawalRequest) {
    CfPayoutsResponse body = performPostRequest(
        PayoutConstants.SELF_WITHDRAWAL_REL_URL, SelfWithdrawalRequest.class, CfPayoutsResponse.class);
    if (HttpStatus.OK.value() == body.getSubCode()) {
      return body;
    }
    throw new UnknownExceptionOccured("Unable to fetch beneficiary id");
  }
}
