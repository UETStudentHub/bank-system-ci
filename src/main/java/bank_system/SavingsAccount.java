package bank_system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tài khoản tiết kiệm, có giới hạn rút tiền và số dư tối thiểu.
 */
public class SavingsAccount extends Account {

  private static final Logger logger = LoggerFactory.getLogger(SavingsAccount.class);

  /** Số tiền tối đa được rút trong một giao dịch. */
  private static final double MAX_WITHDRAWAL_AMOUNT = 1000.0;

  /** Số dư tối thiểu phải còn lại sau khi rút tiền. */
  private static final double MIN_BALANCE = 5000.0;

  /**
   * Tạo tài khoản tiết kiệm mới.
   *
   * @param accountNumber số tài khoản
   * @param balance số dư ban đầu
   */
  public SavingsAccount(long accountNumber, double balance) {
    super(accountNumber, balance);
  }

  /**
   * Nạp tiền vào tài khoản tiết kiệm.
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
          Transaction.TYPE_DEPOSIT_SAVINGS, amount, initialBalance, finalBalance);
      addTransaction(transaction);
      logger.info("Nạp tiền vào tài khoản {} thành công: +{}, số dư: {} -> {}",
          getAccountNumber(), amount, initialBalance, finalBalance);
    } catch (BankException e) {
      logger.error("Nạp tiền vào tài khoản {} thất bại.", getAccountNumber(), e);
    }
  }

  /**
   * Rút tiền khỏi tài khoản tiết kiệm.
   *
   * @param amount số tiền cần rút
   */
  @Override
  public void withdraw(double amount) {
    logger.debug("Đang xử lý giao dịch rút tiền cho tài khoản {}.", getAccountNumber());
    double initialBalance = getBalance();
    try {
      if (amount > MAX_WITHDRAWAL_AMOUNT) {
        throw new InvalidFundingAmountException(amount);
      }
      if (initialBalance - amount < MIN_BALANCE) {
        throw new InsufficientFundsException(amount);
      }
      doWithdrawing(amount);
      double finalBalance = getBalance();
      Transaction transaction = new Transaction(
          Transaction.TYPE_WITHDRAW_SAVINGS, amount, initialBalance, finalBalance);
      addTransaction(transaction);
      logger.info("Rút tiền khỏi tài khoản {} thành công: -{}, số dư: {} -> {}",
          getAccountNumber(), amount, initialBalance, finalBalance);
    } catch (BankException e) {
      logger.error("Rút tiền khỏi tài khoản {} thất bại.", getAccountNumber(), e);
    }
  }
}
