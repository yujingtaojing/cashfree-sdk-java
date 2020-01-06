package com.cashfree.lib.exceptions;

public class ResourceAlreadyExistsException extends RuntimeException {
  public ResourceAlreadyExistsException() { super(""); }

  public ResourceAlreadyExistsException(String str) {
    super();
  }
}
