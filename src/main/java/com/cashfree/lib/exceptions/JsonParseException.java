package com.cashfree.lib.exceptions;

public class JsonParseException extends RuntimeException {
  public JsonParseException() { super(""); }

  public JsonParseException(String str) {
    super(str);
  }

  public JsonParseException(Exception x) {
    super(x);
  }
}
