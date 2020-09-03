package kz.pcdp.сoncurrent.bank;

import static edu.rice.pcdp.PCDP.isolated;

public final class BankTransactionsUsingGlobalIsolation extends ThreadSafeBankTransaction {

  @Override
  public void issueTransfer(final int amount, final Account src, final Account dst) {
    isolated(() -> { src.performTransfer(amount, dst); });
  }
}
