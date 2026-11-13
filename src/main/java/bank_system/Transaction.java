package bank_system;

import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Đại diện cho một giao dịch trên tài khoản.
 */
public class Transaction {

  private static final Logger logger = LoggerFactory.getLogger(Transaction.class);

  /** Nạp tiền vào tài khoản vãng lai. */
  public static final int TYPE_DEPOSIT_CHECKING = 1;

  /** Rút tiền khỏi tài khoản vãng lai. */
  public static final int TYPE_WITHDRAW_CHECKING = 2;

  /** Nạp tiền vào tài khoản tiết kiệm. */
  public static final int TYPE_DEPOSIT_SAVINGS = 3;

  /** Rút tiền khỏi tài khoản tiết kiệm. */
  public static final int TYPE_WITHDRAW_SAVINGS = 4;

  private int type;
  private double amount;
  private double initialBalance;
  private double finalBalance;

  /**
   * Tạo một giao dịch mới.
   *
   * @param type loại giao dịch
   * @param amount số tiền giao dịch
   * @param initialBalance số dư trước giao dịch
   * @param finalBalance số dư sau giao dịch
   */
  public Transaction(int type, double amount, double initialBalance, double finalBalance) {
    this.type = type;
    this.amount = amount;
    this.initialBalance = initialBalance;
    this.finalBalance = finalBalance;
  }

  /**
   * Trả về loại giao dịch.
   *
   * @return loại giao dịch
   */
  public int getType() {
    return type;
  }

  /**
   * Đặt loại giao dịch mới.
   *
   * @param type loại giao dịch mới
   */
  public void setType(int type) {
    this.type = type;
  }

  /**
   * Trả về số tiền giao dịch.
   *
   * @return số tiền giao dịch
   */
  public double getAmount() {
    return amount;
  }

  /**
   * Đặt số tiền giao dịch mới.
   *
   * @param amount số tiền giao dịch mới
   */
  public void setAmount(double amount) {
    this.amount = amount;
  }

  /**
   * Trả về số dư trước giao dịch.
   *
   * @return số dư trước giao dịch
   */
  public double getInitialBalance() {
    return initialBalance;
  }

  /**
   * Đặt số dư trước giao dịch.
   *
   * @param initialBalance số dư trước giao dịch
   */
  public void setInitialBalance(double initialBalance) {
    this.initialBalance = initialBalance;
  }

  /**
   * Trả về số dư sau giao dịch.
   *
   * @return số dư sau giao dịch
   */
  public double getFinalBalance() {
    return finalBalance;
  }

  /**
   * Đặt số dư sau giao dịch.
   *
   * @param finalBalance số dư sau giao dịch
   */
  public void setFinalBalance(double finalBalance) {
    this.finalBalance = finalBalance;
  }

  /**
   * Trả về tên hiển thị của một loại giao dịch.
   *
   * @param transactionType mã loại giao dịch
   * @return tên loại giao dịch
   */
  public static String getTypeString(int transactionType) {
    switch (transactionType) {
      case TYPE_DEPOSIT_CHECKING:
        return "Nạp tiền vãng lai";
      case TYPE_WITHDRAW_CHECKING:
        return "Rút tiền vãng lai";
      case TYPE_DEPOSIT_SAVINGS:
        return "Nạp tiền tiết kiệm";
      case TYPE_WITHDRAW_SAVINGS:
        return "Rút tiền tiết kiệm";
      default:
        return "Không rõ";
    }
  }

  /**
   * Trả về bản tóm tắt giao dịch dưới dạng chuỗi.
   *
   * @return chuỗi tóm tắt giao dịch
   */
  public String getTransactionSummary() {
    logger.debug("Đang tổng hợp tóm tắt cho giao dịch loại: {}", type);
    String typeName = getTypeString(type);
    String initialText = String.format(Locale.US, "%.2f", initialBalance);
    String amountText = String.format(Locale.US, "%.2f", amount);
    String finalText = String.format(Locale.US, "%.2f", finalBalance);
    return String.format(Locale.US,
        "- Kiểu giao dịch: %s. Số dư ban đầu: $%s. Số tiền: $%s. Số dư cuối: $%s.",
        typeName, initialText, amountText, finalText);
  }
}
