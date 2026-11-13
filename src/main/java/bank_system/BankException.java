package bank_system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Ngoại lệ chung trong hệ thống ngân hàng.
 */
public class BankException extends Exception {

  private static final Logger logger = LoggerFactory.getLogger(BankException.class);

  /**
   * Tạo ngoại lệ với thông báo cho trước.
   *
   * @param message thông báo lỗi
   */
  public BankException(String message) {
    super(message);
  }
}
