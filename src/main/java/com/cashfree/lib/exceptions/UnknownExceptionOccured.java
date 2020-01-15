package com.cashfree.lib.exceptions;

public class UnknownExceptionOccured extends RuntimeException {

  public UnknownExceptionOccured() {
      super("Unknown Exception Occured. Please reach out to care@cashfreemail.com");
  }

  public UnknownExceptionOccured(String str) {
    super(str + " Please reach out to care@cashfreemail.com");
  }
}
