package com.cashfree.lib.exceptions;

public class IllegalPayloadException extends RuntimeException {
  public IllegalPayloadException() { super(""); }

  public IllegalPayloadException(String str) {
    super(str);
  }
}
