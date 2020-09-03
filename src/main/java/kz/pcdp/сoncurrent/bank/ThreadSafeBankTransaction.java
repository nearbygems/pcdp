package kz.pcdp.сoncurrent.bank;

public abstract class ThreadSafeBankTransaction {

  public abstract void issueTransfer(final int amount, final Account src, final Account dst);
}
