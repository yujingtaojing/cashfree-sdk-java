package com.cashfree.lib.pg.clients;

import java.io.*;
import java.net.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.lang.reflect.Field;
import java.lang.annotation.Annotation;

import com.cashfree.lib.http.HttpUtils;
import com.cashfree.lib.annotations.NotNull;
import com.cashfree.lib.constants.Constants;
import com.cashfree.lib.exceptions.UnknownExceptionOccured;

import com.cashfree.lib.http.ObjectWriterUtils;
import com.cashfree.lib.pg.constants.Endpoints;
import com.cashfree.lib.http.ObjectMappingUtils;
import com.cashfree.lib.pg.constants.PgConstants;
import com.cashfree.lib.pg.domains.response.VerifyCredentialsResponse;

public class Pg {
  private String appId;
  private String secretKey;

  private String endpoint;

  private static Pg SINGLETON_INSTANCE;

  private Pg(Constants.Environment env, String appId, String secretKey) {
    this.appId = appId;
    this.secretKey = secretKey;

    if (Constants.Environment.PRODUCTION.equals(env)) {
      this.endpoint = Endpoints.PROD_ENDPOINT;
    } else if (Constants.Environment.TEST.equals(env)) {
      this.endpoint = Endpoints.TEST_ENDPOINT;
    }
  }

  public static Pg getInstance(Constants.Environment env, String appId, String secretKey) {
    if (SINGLETON_INSTANCE == null) {
      SINGLETON_INSTANCE = new Pg(env, appId, secretKey);
    }
    return SINGLETON_INSTANCE;
  }

  private Map<String, String> buildPostHeader() {
    Map<String, String> map = new HashMap<>();
    map.put("Content-Type", "application/x-www-form-urlencoded");
    return map;
  }

  @SuppressWarnings("all")
  private String buildEncodedPayload(Map<String, String> payload) {
    try {
      StringBuilder encodedData = new StringBuilder()
          .append(URLEncoder.encode("appId", "UTF-8"))
          .append("=").append(URLEncoder.encode(appId, "UTF-8"))
          .append("&").append(URLEncoder.encode("secretKey", "UTF-8"))
          .append("=").append(URLEncoder.encode(secretKey, "UTF-8"));
      for (Map.Entry<String, String> entry : payload.entrySet()) {
        encodedData
            .append("&").append(URLEncoder.encode(entry.getKey(), "UTF-8"))
            .append("=").append(URLEncoder.encode(entry.getValue(), "UTF-8"));
      }
      return encodedData.toString();
    } catch (UnsupportedEncodingException x) {
      throw new RuntimeException(x);
    }
  }

  public boolean verifyCredentials() {
    VerifyCredentialsResponse body =
        performPostRequest(
            PgConstants.CREDENTIALS_VERIFY_REL_URL, null, null, VerifyCredentialsResponse.class);

    if (body == null) {
      throw new UnknownExceptionOccured();
    }
    return "OK".equals(body.getStatus());
  }

  <Request, Response> Response performPostRequest(
      String relUrl, Request request, Class<Request> requestClass, Class<Response> responseClass) {
    Map<String, String> requestAsMap = getObjectAsMap(request, requestClass);

    return performPostRequest(relUrl, requestAsMap, responseClass);
  }

  <Response> Response performPostRequest(
      String relUrl, Map<String, String> requestAsMap, Class<Response> responseClass) {
    String encodedString = buildEncodedPayload(requestAsMap);

    Response body =
        HttpUtils.performPostRequest(endpoint + relUrl, buildPostHeader(), encodedString, responseClass);

    if (body == null) {
      throw new UnknownExceptionOccured();
    }
    return body;
  }

  private static <T> Map<String, String> getObjectAsMap(T object, Class<T> clazz) {
    if (object == null) return Collections.emptyMap();
    Map<String, String> map = new HashMap<>();
    for (Field field: ObjectMappingUtils.getAllFields(clazz)) {
      Object fieldValue = null;
      Annotation annotation = field.getAnnotation(NotNull.class);
      if (annotation != null) {
        // Field is a required field.
        fieldValue = ObjectWriterUtils.getField(field, object);
        if (fieldValue == null) {
          throw new IllegalArgumentException("Field: " + field.getName() + " of type: " + field.getType() +
              " in object of class: " + clazz.getName() + " can't be null as it is a required field.");
        }
      }
      if (fieldValue != null) {
        map.put(field.getName(), fieldValue.toString());
      }
    }
    return map;
  }
}
