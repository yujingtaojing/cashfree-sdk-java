package com.cashfree.lib.utils;

public class CommonUtils {
  private static final String EMPTY_STRING = "";

  public static boolean isBlank(String str) {
    if (str == null) return true;
    return EMPTY_STRING.equals(str.trim());
  }

  public static boolean isNotBlank(String str) {
    return !isBlank(str);
  }
}
