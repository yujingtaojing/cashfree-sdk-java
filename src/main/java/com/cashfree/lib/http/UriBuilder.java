package com.cashfree.lib.http;

import java.util.Map;
import java.util.HashMap;
//import javax.ws.rs.core.UriBuilder;


/**
 * Primitive uri builder.
 */
public class UriBuilder {
  private String uri;

  private Map<String, String> paramsMap;

  private UriBuilder(String uri) {
    this.uri = uri;
  }

  public static UriBuilder fromUriString(String rootUri) {
    return new UriBuilder(rootUri);
  }

  public UriBuilder queryParam(String paramName, String paramValue) {
    if (paramsMap == null) {
      paramsMap = new HashMap<>();
    }
    paramValue = paramValue.replace(" ", "%20");
    paramsMap.put(paramName, paramValue);
    return this;
  }

  public String toUriString() {
    StringBuilder paramsString = new StringBuilder();
    if (paramsMap != null) {
      boolean isFirstParamSet = false;
      for (Map.Entry<String, String > entry: paramsMap.entrySet()) {
        if (!isFirstParamSet) {
          paramsString.append("?");
          isFirstParamSet = true;
        } else {
          paramsString.append("&");
        }
        paramsString.append(entry.getKey()).append("=").append(entry.getValue());
      }
    }
    return uri + paramsString;
  }
}
