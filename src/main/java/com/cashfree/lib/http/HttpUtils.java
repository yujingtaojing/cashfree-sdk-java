package com.cashfree.lib.http;

import java.util.Map;
import java.io.IOException;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import java.net.URL;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.MalformedURLException;

import com.cashfree.lib.utils.Pair;
import com.cashfree.lib.utils.CommonUtils;
import com.cashfree.lib.utils.ImmutablePair;

public class HttpUtils {
  private static URL getURLFromString(String urlString) {
    URL url;
    try {
      url = new URL(urlString);
    } catch (MalformedURLException x) {
      throw new RuntimeException(x);
    }
    return url;
  }

  private static HttpURLConnection openConnection(URL url) {
    HttpURLConnection conn;
    try {
      conn = (HttpURLConnection) url.openConnection();
    } catch (IOException x) {
      throw new RuntimeException(x);
    }
    conn.setRequestProperty("Content-Type", "application/json; utf-8");
    conn.setRequestProperty("Accept", "application/json");
    conn.setRequestProperty("cache-control", "no-cache");
    conn.setRequestProperty("User-Agent", "Cashfree-SDK");
    return conn;
  }

  private static HttpURLConnection openGetConnection(URL url) {
    HttpURLConnection conn = openConnection(url);
    try {
      conn.setRequestMethod("GET");
    } catch (ProtocolException x) {
      throw new RuntimeException(x);
    }
    return conn;
  }

  private static HttpURLConnection openPostConnection(URL url) {
    HttpURLConnection conn = openConnection(url);
    try {
      conn.setRequestMethod("POST");
    } catch (ProtocolException x) {
      throw new RuntimeException(x);
    }
    return conn;
  }

  private static void setHeadersToConnection(HttpURLConnection conn, Map<String, String> headers) {
    for (Map.Entry<String, String> entry: headers.entrySet()) {
      conn.setRequestProperty(entry.getKey(), entry.getValue());
    }
  }

  private static int sendDataToUrlConn(HttpURLConnection conn, String request) {
    conn.setDoOutput(true);
    try {
      // Only for POST.
      if (CommonUtils.isNotBlank(request)) {
        OutputStream os = null;
        os = conn.getOutputStream();
        os.write(request.getBytes());
        os.flush();
        os.close();
      }
      return conn.getResponseCode();
    } catch (IOException x) {
      throw new RuntimeException(x);
    }
  }

  private static String extractResponseFromUrlConn(HttpURLConnection conn) {
    StringBuilder response = new StringBuilder();
    try(BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
      String responseLine = null;
      while ((responseLine = br.readLine()) != null) {
        response.append(responseLine.trim());
      }
      response = new StringBuilder(response.substring(0, response.length() - 1));
      response.append(",\"xRequestId\":\"");
      response.append(conn.getHeaderField("X-Request-Id"));
      response.append("\"}");
    } catch (IOException x) {
      throw new RuntimeException(x);
    }
    return response.toString();
  }

  private static Pair<Integer, String> fetchPostResponseFromServer(HttpURLConnection conn, String request) {
    int responseCode = sendDataToUrlConn(conn, request);
    String response = extractResponseFromUrlConn(conn);
    return new ImmutablePair<>(responseCode, response);
  }

  private static Pair<Integer, String> fetchGetResponseFromServer(HttpURLConnection conn) {
    int responseCode = sendDataToUrlConn(conn, null);
    String response = extractResponseFromUrlConn(conn);

    return new ImmutablePair<>(responseCode, response);
  }

  public static <Req, Resp> Resp performPostRequest(
      String urlString, Map<String, String> headers, Req request, Class<Resp> respClass) {
    URL url = HttpUtils.getURLFromString(urlString);
    HttpURLConnection conn = HttpUtils.openPostConnection(url);

    if (headers != null && headers.size() > 0) {
      HttpUtils.setHeadersToConnection(conn, headers);
    }

    Pair<Integer, String> cashfreeResponse = HttpUtils.fetchPostResponseFromServer(conn, ObjectMapper.write(request));
    conn.disconnect();

    if (cashfreeResponse.getLeft() != 200) {
      // Non success response. Do something.
    }

    return ObjectMapper.readValue(cashfreeResponse.getRight(), respClass);
  }

  public static <Resp> Resp performGetRequest(
      String urlString, Map<String, String> headers, Class<Resp> respClass) {
    URL url = HttpUtils.getURLFromString(urlString);
    HttpURLConnection conn = HttpUtils.openGetConnection(url);

    if (headers != null && headers.size() > 0) {
      HttpUtils.setHeadersToConnection(conn, headers);
    }

    Pair<Integer, String> cashfreeResponse = HttpUtils.fetchGetResponseFromServer(conn);
    conn.disconnect();

    if (cashfreeResponse.getLeft() != 200) {
      // Non success response. Do something.
    }

    return ObjectMapper.readValue(cashfreeResponse.getRight(), respClass);
  }
}
