package bank_system;

import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Ngoại lệ khi số dư tài khoản không đủ.
 */
public class InsufficientFundsException extends BankException {

  private static final Logger logger =
      LoggerFactory.getLogger(InsufficientFundsException.class);

  /**
   * Tạo ngoại lệ khi số dư không đủ để thực hiện giao dịch.
   *
   * @param amount số tiền giao dịch
   */
  public InsufficientFundsException(double amount) {
    super("Số dư tài khoản không đủ $" + String.format(Locale.US, "%.2f", amount)
        + " để thực hiện giao dịch");
  }
}
