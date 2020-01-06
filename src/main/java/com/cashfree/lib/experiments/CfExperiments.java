package com.cashfree.lib.experiments;

import java.io.IOException;

import com.cashfree.lib.clients.Payouts;
import com.cashfree.lib.clients.Beneficiary;
import com.cashfree.lib.domains.BeneficiaryDetails;
import com.cashfree.lib.constants.Constants.Environment;

public class CfExperiments {
  public static void main(String[] args) throws IOException {
    Payouts payouts = new Payouts(
        Environment.PRODUCTION, "CF1848EZPSGLHWP9IUE2Y", "b8df7784dd3f38911294d3597764dd43f3016a48");

    Beneficiary beneficiary = new Beneficiary(payouts);
    payouts.updateBearerToken();
    System.out.println(payouts.verifyToken());

    BeneficiaryDetails beneficiaryDetails = new BeneficiaryDetails()
        .setBeneId("JOHN18012")
        .setName("john doe")
        .setEmail("johndoe@cashfree.com")
        .setPhone("9876543210")
        .setBankAccount("00001111222233")
        .setIfsc("HDFC0000001")
        .setAddress1("ABC Street")
        .setCity("Bangalore")
        .setState("Karnataka")
        .setPincode("560001");

    System.out.println(beneficiary.addBeneficiary(beneficiaryDetails));
  }
}
