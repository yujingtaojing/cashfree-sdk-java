package com.cashfree.lib.pg.constants;

public class PgConstants {
  private static final String PG_REL_URL = "/api";

  private static final String VERSION = "/v1";

  public static final String ORDER_CREATE_REL_URL = PG_REL_URL + VERSION + "/order/create";

  public static final String ORDER_GET_LINK_REL_URL = PG_REL_URL + VERSION + "/order/info/link";

  public static final String ORDER_GET_DETAILS_REL_URL = PG_REL_URL + VERSION + "/order/info";

  public static final String ORDER_GET_STATUS_REL_URL = PG_REL_URL + VERSION + "/order/info/status";

  public static final String ORDER_EMAIL_REL_URL = PG_REL_URL + VERSION + "/order/email";

  public static final String TRANSACTIONS_REL_URL = PG_REL_URL + VERSION + "/transactions";

  public static final String REFUNDS_REL_URL = PG_REL_URL + VERSION + "/refunds";

  public static final String ORDER_REFUND_REL_URL = PG_REL_URL + VERSION + "/order/refund";

  public static final String REFUND_STATUS_REL_URL = PG_REL_URL + VERSION + "/refundStatus";

  public static final String SETTLEMENTS_REL_URL = PG_REL_URL + VERSION + "/settlements";

  public static final String SETTLEMENT_REL_URL = PG_REL_URL + VERSION + "/settlement";

  public static final String CREDENTIALS_VERIFY_REL_URL = PG_REL_URL + VERSION + "/credentials/verify";
}
