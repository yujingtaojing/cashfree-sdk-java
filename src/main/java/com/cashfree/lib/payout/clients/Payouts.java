package com.cashfree.lib.payout.clients;

import java.io.File;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.*;

import com.cashfree.lib.constants.Constants;
import com.cashfree.lib.exceptions.SignatureCreationFailedException;
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
import com.cashfree.lib.utils.ExceptionThrower;
import org.apache.http.client.utils.URIBuilder;

import javax.crypto.Cipher;

import static com.cashfree.lib.constants.Constants.IP;
import static com.cashfree.lib.constants.Constants.SIGNATURE;

public class Payouts {

  private String clientId;
  private String clientSecret;
  private String publicKeyPath;
  private static String mode;
  private static String endpoint;
  private volatile String bearerToken;

  private Long expiry;
  private static Payouts SINGLETON_INSTANCE;

  private Payouts(Environment env, String clientId, String clientSecret) {
    this.clientId = clientId;
    this.clientSecret = clientSecret;
    this.mode = IP;
    if (Environment.PRODUCTION.equals(env)) {
      this.endpoint = Endpoints.PROD_ENDPOINT;
    } else if (Environment.TEST.equals(env)) {
      this.endpoint = Endpoints.TEST_ENDPOINT;
    }
  }

  private Payouts(Environment env, String clientId, String clientSecret , String publicKeyPath) {
    this.clientId = clientId;
    this.clientSecret = clientSecret;
    if (publicKeyPath.length() == 0)
      this.mode = IP ;
    else
      this.mode = SIGNATURE;

    if (Environment.PRODUCTION.equals(env)) {
      this.endpoint = Endpoints.PROD_ENDPOINT;
    } else if (Environment.TEST.equals(env)) {
      this.endpoint = Endpoints.TEST_ENDPOINT;
    }
    this.publicKeyPath = publicKeyPath;
  }

  public static String getEndpoint() { return endpoint;}

  public static Payouts  getInstance(Environment env, String clientId, String clientSecret, String pubilcKeyPath) {
    if (SINGLETON_INSTANCE == null) {
      if (pubilcKeyPath.length() == 0){
        SINGLETON_INSTANCE = new Payouts(env, clientId, clientSecret);
      }
      else
      {
        SINGLETON_INSTANCE = new Payouts(env, clientId, clientSecret , pubilcKeyPath);
      }
    }
    return SINGLETON_INSTANCE;
  }

  public static Payouts getInstance(Environment env, String clientId, String clientSecret ) {
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

  synchronized void updateBearerToken() {
    // Setup headers.
    Map<String, String> headersMap = new HashMap<>();
    headersMap.put("X-Client-Id", clientId);
    headersMap.put("X-Client-Secret", clientSecret);

    if (mode == "SIGNATURE"){
      String signature = generateEncryptedSignature(clientId, publicKeyPath);
      if (signature == ""){
        throw new SignatureCreationFailedException();
      }
      headersMap.put("X-Cf-Signature", signature);
    }

    URIBuilder uriBuilder = null;
    try {
      uriBuilder =
              new URIBuilder(Payouts.getEndpoint() + PayoutConstants.AUTH_REL_URL);
    } catch (Exception e) {
     throw new UnknownExceptionOccured(e.getMessage());
    }
    AuthenticationResponse body =
        HttpUtils.performPostRequest(
                uriBuilder.toString(), headersMap, null, AuthenticationResponse.class);

    if (body == null) {
      throw new UnknownExceptionOccured();
    }
    if (200 == body.getSubCode() || 201 == body.getSubCode() || 202 == body.getSubCode()) {
      if (body.getData() == null) {
        throw new UnknownExceptionOccured();
      }
      bearerToken = body.getData().getToken();
      expiry = body.getData().getExpiry();
    }
    ExceptionThrower.throwException(body.getSubCode() ,
            body.getXRequestId(),
            body.getMessage());
  }

  public boolean verifyToken() {
    Map<String, String> authHeaders = buildAuthHeader();
    URIBuilder uriBuilder = null;
    try {
      uriBuilder =
              new URIBuilder(Payouts.getEndpoint() + PayoutConstants.VERIFY_TOKEN_REL_URL);
    } catch (Exception e) {
     throw new UnknownExceptionOccured(e.getMessage());
    }
    CfPayoutsResponse body =
        HttpUtils.performPostRequest(
                uriBuilder.toString(), authHeaders, null, CfPayoutsResponse.class);

    if (body == null) {
      throw new UnknownExceptionOccured();
    }
    if (200 == body.getSubCode() || 201 == body.getSubCode() || 202 == body.getSubCode()) {
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
        HttpUtils.performPostRequest( relUrl, authHeaders, request, clazz);

    if (body == null) {
      throw new UnknownExceptionOccured();
    }
    // TODO Return this as it is. Make change in the core api itself instead of adding a fix.
    if (body.getSubCode() == null) {
      return body;
    }
    if (403 == body.getSubCode()) {
      updateBearerToken();
      return performPostRequest(relUrl, request, clazz);
    }
    return body;
  }

  <Response extends CfPayoutsResponse> Response performGetRequest(String relUrl, Class<Response> clazz) {
    Map<String, String> authHeaders = buildAuthHeader();
    Response body =
        HttpUtils.performGetRequest(relUrl, authHeaders, clazz);
    if (body == null) {
      throw new UnknownExceptionOccured();
    }
    if (403 == body.getSubCode()) {
      updateBearerToken();
      return performGetRequest(relUrl, clazz);
    }
    return body;
  }

  public GetBalanceResponse getBalance() {
    URIBuilder uriBuilder = null;
    try {
      uriBuilder =
              new URIBuilder(Payouts.getEndpoint() + PayoutConstants.GET_BALANCE_REL_URL);
    } catch (Exception e) {
     throw new UnknownExceptionOccured(e.getMessage());
    }
    GetBalanceResponse body = performGetRequest(
            uriBuilder.toString(), GetBalanceResponse.class);
    if (200 == body.getSubCode() || 201 == body.getSubCode() || 202 == body.getSubCode()) {
      return body;
    }
    ExceptionThrower.throwException(body.getSubCode() ,
            body.getXRequestId(),
            "Unable to fetch beneficiary id\n." + body.getMessage());
    return body;
  }

  public SelfWithdrawalResponse selfWithdrawal(SelfWithdrawalRequest selfWithdrawalRequest) {
    URIBuilder uriBuilder = null;
    try {
      uriBuilder =
              new URIBuilder(Payouts.getEndpoint() + PayoutConstants.SELF_WITHDRAWAL_REL_URL);
    } catch (Exception e) {
     throw new UnknownExceptionOccured(e.getMessage());
    }
    SelfWithdrawalResponse body = performPostRequest(
            uriBuilder.toString(), selfWithdrawalRequest, SelfWithdrawalResponse.class);

    if (200 == body.getStatusCode() || 400 == body.getStatusCode()) {
      return body;
    }
    ExceptionThrower.throwException(body.getSubCode() ,
            body.getXRequestId(),
            body.getMessage());
    return  body;
  }

  private String generateEncryptedSignature(String clientId , String pathname) {
    String clientIdWithEpochTimeStamp = clientId+"."+ Instant.now().getEpochSecond();
    String encrytedSignature = "";
    try {
      byte[] keyBytes = Files
              .readAllBytes(new File(pathname).toPath()); // Absolute Path to be replaced

      String publicKeyContent = new String(keyBytes);
      publicKeyContent = publicKeyContent.replaceAll("[\\t\\n\\r]", "")
              .replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "");
      KeyFactory kf = KeyFactory.getInstance("RSA");
      X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(
              Base64.getDecoder().decode(publicKeyContent));
      RSAPublicKey pubKey = (RSAPublicKey) kf.generatePublic(keySpecX509);
      final Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
      cipher.init(Cipher.ENCRYPT_MODE, pubKey);
      encrytedSignature = Base64.getEncoder().encodeToString(cipher.doFinal(clientIdWithEpochTimeStamp.getBytes()));
    } catch (Exception e) {
      throw new SignatureCreationFailedException(e.getMessage());
    }
    return encrytedSignature;
  }
}
