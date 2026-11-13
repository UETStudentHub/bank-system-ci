package bank_system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tài khoản vãng lai.
 */
public class CheckingAccount extends Account {

  private static final Logger logger = LoggerFactory.getLogger(CheckingAccount.class);

  /**
   * Tạo tài khoản vãng lai mới.
   *
   * @param accountNumber số tài khoản
   * @param balance số dư ban đầu
   */
  public CheckingAccount(long accountNumber, double balance) {
    super(accountNumber, balance);
  }

  /**
   * Nạp tiền vào tài khoản vãng lai.
   *
   * @param amount số tiền cần nạp
   */
  @Override
  public void deposit(double amount) {
    logger.debug("Đang xử lý giao dịch nạp tiền cho tài khoản {}.", getAccountNumber());
    double initialBalance = getBalance();
    try {
      doDepositing(amount);
      double finalBalance = getBalance();
      Transaction transaction = new Transaction(
          Transaction.TYPE_DEPOSIT_CHECKING, amount, initialBalance, finalBalance);
      addTransaction(transaction);
      logger.info("Nạp tiền vào tài khoản {} thành công: +{}, số dư: {} -> {}",
          getAccountNumber(), amount, initialBalance, finalBalance);
    } catch (BankException e) {
      logger.error("Nạp tiền vào tài khoản {} thất bại.", getAccountNumber(), e);
    }
  }

  /**
   * Rút tiền khỏi tài khoản vãng lai.
   *
   * @param amount số tiền cần rút
   */
  @Override
  public void withdraw(double amount) {
    logger.debug("Đang xử lý giao dịch rút tiền cho tài khoản {}.", getAccountNumber());
    double initialBalance = getBalance();
    try {
      doWithdrawing(amount);
      double finalBalance = getBalance();
      Transaction transaction = new Transaction(
          Transaction.TYPE_WITHDRAW_CHECKING, amount, initialBalance, finalBalance);
      addTransaction(transaction);
      logger.info("Rút tiền khỏi tài khoản {} thành công: -{}, số dư: {} -> {}",
          getAccountNumber(), amount, initialBalance, finalBalance);
    } catch (BankException e) {
      logger.error("Rút tiền khỏi tài khoản {} thất bại.", getAccountNumber(), e);
    }
  }
}
