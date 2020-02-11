package com.cashfree.lib.http;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;

import com.cashfree.lib.utils.CommonUtils;

public class ObjectMapper {
  public static <T> T readValue(String str, Class<T> clazz) {
    if (CommonUtils.isBlank(str)) return null;
    JsonValue value = Json.parse(str);
    return ObjectReaderUtils.readValue(value, clazz);
  }

  public static <T> String write(T t) {
    if (t == null) return null;

    if (t instanceof String) return (String) t;
    return ObjectWriterUtils.writeValue(t);
  }
}
