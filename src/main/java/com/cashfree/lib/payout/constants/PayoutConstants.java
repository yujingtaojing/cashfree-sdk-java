package com.cashfree.lib.payout.constants;

public class PayoutConstants {
  private static final String PAYOUT_REL_URL = "/payout";

  private static final String VERSION = "/v1";

  public static final String AUTH_REL_URL = PAYOUT_REL_URL + VERSION + "/authorize";

  public static final String VERIFY_TOKEN_REL_URL = PAYOUT_REL_URL + VERSION + "/verifyToken";

  public static final String ADD_BENEFICIARY_REL_URL = PAYOUT_REL_URL + VERSION + "/addBeneficiary";

  public static final String CREATE_GROUP_REL_URL = PAYOUT_REL_URL + VERSION + "/createGroup";

  public static final String GET_BENEFICIARY_REL_URL = PAYOUT_REL_URL + VERSION + "/getBeneficiary";

  public static final String GET_BENE_ID_REL_URL = PAYOUT_REL_URL + VERSION + "/getBeneId";

  public static final String REMOVE_BENEFICIARY_REL_URL = PAYOUT_REL_URL + VERSION + "/removeBeneficiary";

  public static final String GET_BALANCE_REL_URL = PAYOUT_REL_URL + VERSION + "/getBalance";

  public static final String REQUEST_TRANSFER_REL_URL = PAYOUT_REL_URL + VERSION + "/requestTransfer";

  public static final String GET_TRANSFER_STATUS_REL_URL = PAYOUT_REL_URL + VERSION + "/getTransferStatus";

//  public static final String GET_TRANSFERS_REL_URL = PAYOUT_REL_URL + VERSION + "/getTransfers";

  public static final String VALIDATION_BANK_DETAILS_REL_URL = PAYOUT_REL_URL + VERSION + "/validation/bankDetails";

  public static final String SELF_WITHDRAWAL_REL_URL = PAYOUT_REL_URL + VERSION + "/selfWithdrawal";

  public static final String VALIDATION_UPI_DETAILS_REL_URL = PAYOUT_REL_URL + VERSION + "/validation/upiDetails";

  public static final String CREATE_CASHGRAM_REL_URL = PAYOUT_REL_URL + VERSION + "/createCashgram";

  public static final String GET_CASHGRAM_STATUS_REL_URL = PAYOUT_REL_URL + VERSION + "/getCashgramStatus";

  public static final String DEACTIVATE_CASHGRAM_REL_URL = PAYOUT_REL_URL + VERSION + "/deactivateCashgram";

  public static final String REQUEST_BATCH_TRANSFER_REL_URL = PAYOUT_REL_URL + VERSION + "/requestBatchTransfer";

  public static final String GET_BATCH_TRANSFER_STATUS_REL_URL = PAYOUT_REL_URL + VERSION + "/getBatchTransferStatus";

  public static final String BULK_VALIDATION_BANK_DETAILS_REL_URL = PAYOUT_REL_URL + VERSION + "/bulkValidation/bankDetails";

  public static final String GET_BULK_VALIDATION_STATUS_REL_URL = PAYOUT_REL_URL + VERSION + "/getBulkValidationStatus";
}
