package com.cashfree.lib.exceptions;

public class ResourceDoesntExistException extends RuntimeException {
  public ResourceDoesntExistException() {
    super("");
  }

  public ResourceDoesntExistException(String str) {
    super(str);
  }
}
