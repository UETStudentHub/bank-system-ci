package bank_system;

import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Ngoại lệ khi số tiền giao dịch không hợp lệ.
 */
public class InvalidFundingAmountException extends BankException {

  private static final Logger logger =
      LoggerFactory.getLogger(InvalidFundingAmountException.class);

  /**
   * Tạo ngoại lệ khi số tiền giao dịch không hợp lệ.
   *
   * @param amount số tiền giao dịch
   */
  public InvalidFundingAmountException(double amount) {
    super("Số tiền không hợp lệ: $" + String.format(Locale.US, "%.2f", amount));
  }
}
