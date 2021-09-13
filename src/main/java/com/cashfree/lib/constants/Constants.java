package com.cashfree.lib.constants;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Constants {
  public static final DateTimeFormatter dateTimeFormatter =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("Asia/Kolkata"));

  public static final String PLACEHOLDER_DATESTRING = "0000-00-00 00:00:00";

  public static final String IP = "IP";
  public static final String SIGNATURE = "SIGNATURE";


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
