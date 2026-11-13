// This is a very long comment line intentionally created to demonstrate a Checkstyle line length violation in the pull request review demo.

package bank_system;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lớp trừu tượng đại diện cho một tài khoản ngân hàng.
 *
 * <p>Mỗi tài khoản có một số tài khoản, một số dư và một danh sách giao dịch.
 * Hai loại tài khoản con là tài khoản vãng lai và tài khoản tiết kiệm.
 */
public abstract class Account {

  private static final Logger logger = LoggerFactory.getLogger(Account.class);

  /** Loại tài khoản vãng lai. */
  public static final String CHECKING_TYPE = "CHECKING";

  /** Loại tài khoản tiết kiệm. */
  public static final String SAVINGS_TYPE = "SAVINGS";

  private long accountNumber;
  private double balance;
  private List<Transaction> transactionList;

  /**
   * Tạo một tài khoản mới.
   *
   * @param accountNumber số tài khoản
   * @param balance số dư ban đầu
   */
  public Account(long accountNumber, double balance) {
    this.accountNumber = accountNumber;
    this.balance = balance;
    this.transactionList = new ArrayList<>();
  }

  /**
   * Trả về số tài khoản.
   *
   * @return số tài khoản
   */
  public long getAccountNumber() {
    return accountNumber;
  }

  /**
   * Đặt số tài khoản mới.
   *
   * @param accountNumber số tài khoản mới
   */
  public void setAccountNumber(long accountNumber) {
    this.accountNumber = accountNumber;
  }

  /**
   * Trả về số dư hiện tại.
   *
   * @return số dư hiện tại
   */
  public double getBalance() {
    return balance;
  }

  /**
   * Đặt số dư mới.
   *
   * @param balance số dư mới
   */
  protected void setBalance(double balance) {
    this.balance = balance;
  }

  /**
   * Trả về danh sách giao dịch của tài khoản.
   *
   * @return danh sách giao dịch
   */
  public List<Transaction> getTransactionList() {
    return transactionList;
  }

  /**
   * Đặt danh sách giao dịch mới cho tài khoản.
   *
   * @param transactionList danh sách giao dịch mới
   */
  public void setTransactionList(List<Transaction> transactionList) {
    if (transactionList == null) {
      this.transactionList = new ArrayList<>();
    } else {
      this.transactionList = transactionList;
    }
  }

  /**
   * Nạp tiền vào tài khoản.
   *
   * @param amount số tiền cần nạp
   */
  public abstract void deposit(double amount);

  /**
   * Rút tiền khỏi tài khoản.
   *
   * @param amount số tiền cần rút
   */
  public abstract void withdraw(double amount);

  /**
   * Cộng tiền vào số dư sau khi kiểm tra số tiền hợp lệ.
   *
   * @param amount số tiền cần nạp
   * @throws InvalidFundingAmountException nếu số tiền không lớn hơn 0
   */
  protected void doDepositing(double amount) throws InvalidFundingAmountException {
    if (amount <= 0) {
      throw new InvalidFundingAmountException(amount);
    }
    balance += amount;
  }

  /**
   * Trừ tiền khỏi số dư sau khi kiểm tra số tiền và số dư.
   *
   * @param amount số tiền cần rút
   * @throws BankException nếu số tiền không hợp lệ hoặc số dư không đủ
   */
  protected void doWithdrawing(double amount) throws BankException {
    if (amount <= 0) {
      throw new InvalidFundingAmountException(amount);
    }
    if (amount > balance) {
      throw new InsufficientFundsException(amount);
    }
    balance -= amount;
  }

  /**
   * Thêm một giao dịch vào lịch sử của tài khoản.
   *
   * @param transaction giao dịch cần thêm
   */
  public void addTransaction(Transaction transaction) {
    if (transaction != null) {
      transactionList.add(transaction);
    }
  }

  /**
   * Trả về lịch sử giao dịch của tài khoản dưới dạng chuỗi.
   *
   * @return chuỗi lịch sử giao dịch
   */
  public String getTransactionHistory() {
    logger.debug("Đã lấy lịch sử cho tài khoản: {}", accountNumber);
    StringBuilder history = new StringBuilder();
    history.append("Lịch sử giao dịch của tài khoản ").append(accountNumber).append(":\n");
    for (int i = 0; i < transactionList.size(); i++) {
      history.append(transactionList.get(i).getTransactionSummary());
      if (i < transactionList.size() - 1) {
        history.append("\n");
      }
    }
    return history.toString();
  }

  /**
   * So sánh tài khoản này với một đối tượng khác theo số tài khoản.
   *
   * @param obj đối tượng cần so sánh
   * @return true nếu hai tài khoản có cùng số tài khoản
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Account)) {
      return false;
    }
    Account other = (Account) obj;
    return this.accountNumber == other.accountNumber;
  }

  /**
   * Trả về mã băm của tài khoản theo số tài khoản.
   *
   * @return mã băm của tài khoản
   */
  @Override
  public int hashCode() {
    return Long.hashCode(accountNumber);
  }
}
