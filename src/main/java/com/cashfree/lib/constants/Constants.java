package com.cashfree.lib.constants;

public class Constants {
  public enum Environment {
    TEST("TEST"),
    PRODUCTION("PRODUCTION");

    private String value;

    Environment(final String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return this.getValue();
    }
  }
}
