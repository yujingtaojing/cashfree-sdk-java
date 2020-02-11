package com.cashfree.lib.experiments;

import java.util.logging.Logger;
import java.util.logging.ConsoleHandler;

import com.cashfree.lib.logger.VerySimpleFormatter;

public class PgExperiments {
  private static final Logger log = Logger.getLogger(PayoutExperiments.class.getName());
  static {
    ConsoleHandler consoleHandler = new ConsoleHandler();
    consoleHandler.setFormatter(new VerySimpleFormatter());
    log.addHandler(consoleHandler);
  }

  public static void main(String[] args) {

  }
}
