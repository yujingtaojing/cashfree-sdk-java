package com.cashfree.lib.serializers;

public interface JsonFieldSerializer<T> {
  public String serialize(T value);
}
