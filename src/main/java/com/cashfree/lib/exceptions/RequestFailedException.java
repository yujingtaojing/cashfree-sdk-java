package com.cashfree.lib.exceptions;

public class RequestFailedException extends RuntimeException{
    public RequestFailedException() { super(""); }

  public RequestFailedException(String str) {
    super(str);
        }
}
