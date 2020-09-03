package kz.pcdp.сoncurrent.bank;

import static edu.rice.pcdp.PCDP.isolated;

public final class BankTransactionsUsingObjectIsolation extends ThreadSafeBankTransaction {

  @Override
  public void issueTransfer(final int amount, final Account src, final Account dst) {

    isolated(src, dst, () -> {
      boolean success = src.withdraw(amount);
      if (success) {
        dst.deposit(amount);
      }
    });
  }
}
