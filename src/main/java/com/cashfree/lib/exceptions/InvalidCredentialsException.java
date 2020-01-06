package com.cashfree.lib.exceptions;

public class InvalidCredentialsException extends RuntimeException {

  public InvalidCredentialsException() {
    super("Invalid clientId and clientSecret combination");
  }
}