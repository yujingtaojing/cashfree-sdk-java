package com.cashfree.lib.serializers;

public interface JsonFieldDeserializer<T> {
  public T deserialize(String value);
}
