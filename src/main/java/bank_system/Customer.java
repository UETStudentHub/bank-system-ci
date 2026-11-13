package bank_system;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lớp Customer đại diện cho một khách hàng.
 */
public class Customer {

  private static final Logger logger = LoggerFactory.getLogger(Customer.class);

  private long idNumber;
  private String fullName;
  private List<Account> accountList;

  /**
   * Tạo một khách hàng mới với mã CMND 0 và tên rỗng.
   */
  public Customer() {
    this(0L, "");
  }

  /**
   * Tạo một khách hàng mới.
   *
   * @param idNumber mã CMND của khách hàng
   * @param fullName họ tên khách hàng
   */
  public Customer(long idNumber, String fullName) {
    this.idNumber = idNumber;
    this.fullName = fullName;
    this.accountList = new ArrayList<>();
  }

  /**
   * Trả về mã CMND của khách hàng.
   *
   * @return mã CMND
   */
  public long getIdNumber() {
    return idNumber;
  }

  /**
   * Đặt mã CMND mới cho khách hàng.
   *
   * @param idNumber mã CMND mới
   */
  public void setIdNumber(long idNumber) {
    this.idNumber = idNumber;
  }

  /**
   * Trả về họ tên khách hàng.
   *
   * @return họ tên khách hàng
   */
  public String getFullName() {
    return fullName;
  }

  /**
   * Đặt họ tên mới cho khách hàng.
   *
   * @param fullName họ tên mới
   */
  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  /**
   * Trả về danh sách tài khoản của khách hàng.
   *
   * @return danh sách tài khoản
   */
  public List<Account> getAccountList() {
    return accountList;
  }

  /**
   * Đặt danh sách tài khoản mới cho khách hàng.
   *
   * @param accountList danh sách tài khoản mới
   */
  public void setAccountList(List<Account> accountList) {
    if (accountList == null) {
      this.accountList = new ArrayList<>();
    } else {
      this.accountList = accountList;
    }
  }

  /**
   * Thêm tài khoản cho khách hàng.
   *
   * @param account tài khoản cần thêm
   */
  public void addAccount(Account account) {
    if (account == null) {
      return;
    }
    if (!accountList.contains(account)) {
      accountList.add(account);
      logger.debug("Đã thêm tài khoản {} cho khách hàng {}.",
          account.getAccountNumber(), fullName);
    } else {
      logger.warn("Tài khoản {} đã tồn tại, không thêm lại.", account.getAccountNumber());
    }
  }

  /**
   * Xóa tài khoản khỏi khách hàng.
   *
   * @param account tài khoản cần xóa
   */
  public void removeAccount(Account account) {
    if (account == null) {
      return;
    }
    accountList.remove(account);
    logger.debug("Đã xóa tài khoản {} khỏi khách hàng {}.",
        account.getAccountNumber(), fullName);
  }

  /**
   * Trả về thông tin khách hàng dạng text.
   *
   * @return chuỗi thông tin khách hàng
   */
  public String getCustomerInfo() {
    return "Số CMND: " + idNumber + ". Họ tên: " + fullName + ".";
  }
}
