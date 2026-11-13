package bank_system;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lớp chính của hệ thống ngân hàng, chạy demo nạp và rút tiền.
 */
public class Main {

  private static final Logger logger = LoggerFactory.getLogger(Main.class);

  /** Không cho tạo đối tượng, lớp này chỉ có hàm main. */
  private Main() {
  }

  /**
   * Chạy demo: tạo một khách hàng với hai tài khoản, nạp và rút tiền.
   *
   * @param args tham số dòng lệnh, không dùng trong demo này
   */
  public static void main(String[] args) {
    logger.info("Ứng dụng BankSystem bắt đầu.");
    Bank bank = new Bank();
    Customer customer = new Customer(111111111L, "Nguyen Van A");
    CheckingAccount checking = new CheckingAccount(1001, 1000);
    SavingsAccount savings = new SavingsAccount(1002, 6000);
    customer.addAccount(checking);
    customer.addAccount(savings);
    bank.setCustomerList(List.of(customer));
    checking.deposit(500);
    savings.withdraw(200);
    logger.info("Số dư tài khoản vãng lai {}: {}", checking.getAccountNumber(),
        checking.getBalance());
    logger.info("Số dư tài khoản tiết kiệm {}: {}", savings.getAccountNumber(),
        savings.getBalance());
    logger.info("Danh sách khách hàng:\n{}", bank.getCustomersInfoByIdOrder());
    logger.info("Ứng dụng BankSystem kết thúc.");
  }
}
