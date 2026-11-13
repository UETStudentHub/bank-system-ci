package bank_system;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lớp Bank quản lý danh sách khách hàng và đọc dữ liệu từ file.
 */
public class Bank {

  private static final Logger logger = LoggerFactory.getLogger(Bank.class);

  /** Biểu thức chính quy kiểm tra số CMND gồm đúng 9 chữ số. */
  private static final String ID_NUMBER_PATTERN = "\\d{9}";

  /** Số trường tối thiểu của một dòng mô tả tài khoản. */
  private static final int MIN_ACCOUNT_PARTS = 3;

  private List<Customer> customerList;

  /**
   * Tạo một ngân hàng mới với danh sách khách hàng rỗng.
   */
  public Bank() {
    this.customerList = new ArrayList<>();
  }

  /**
   * Trả về danh sách khách hàng.
   *
   * @return danh sách khách hàng
   */
  public List<Customer> getCustomerList() {
    return customerList;
  }

  /**
   * Đặt danh sách khách hàng mới.
   *
   * @param customerList danh sách khách hàng mới
   */
  public void setCustomerList(List<Customer> customerList) {
    if (customerList == null) {
      this.customerList = new ArrayList<>();
    } else {
      this.customerList = customerList;
    }
  }

  /**
   * Đọc danh sách khách hàng và tài khoản từ luồng dữ liệu đầu vào.
   *
   * <p>Mỗi khách hàng bắt đầu bằng một dòng gồm họ tên và số CMND 9 chữ số.
   * Các dòng sau đó mô tả tài khoản của khách hàng đó.
   *
   * @param inputStream luồng dữ liệu đầu vào
   */
  public void readCustomerList(InputStream inputStream) {
    logger.debug("Bắt đầu đọc dữ liệu khách hàng.");
    if (inputStream == null) {
      logger.warn("Luồng dữ liệu đầu vào là null, không đọc được khách hàng nào.");
      return;
    }
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
      Customer current = null;
      String line;
      while ((line = reader.readLine()) != null) {
        current = processLine(line.trim(), current);
      }
    } catch (IOException | NumberFormatException e) {
      logger.error("Lỗi khi đọc danh sách khách hàng.", e);
    }
  }

  /**
   * Xử lý một dòng dữ liệu và trả về khách hàng hiện tại.
   *
   * @param line dòng dữ liệu đã cắt khoảng trắng thừa
   * @param current khách hàng đang đọc dở, có thể là null
   * @return khách hàng hiện tại sau khi xử lý dòng này
   */
  private Customer processLine(String line, Customer current) {
    logger.debug("Đang xử lý một dòng dữ liệu.");
    if (line.isEmpty()) {
      return current;
    }
    int lastSpace = line.lastIndexOf(' ');
    if (lastSpace <= 0) {
      logger.warn("Bỏ qua một dòng không đúng định dạng.");
      return current;
    }
    String token = line.substring(lastSpace + 1).trim();
    if (token.matches(ID_NUMBER_PATTERN)) {
      return addCustomer(line.substring(0, lastSpace).trim(), token);
    }
    addAccount(current, line);
    return current;
  }

  /**
   * Tạo khách hàng mới từ tên và số CMND rồi thêm vào danh sách.
   *
   * @param name họ tên khách hàng
   * @param idText số CMND dạng chuỗi
   * @return khách hàng vừa tạo
   */
  private Customer addCustomer(String name, String idText) {
    Customer customer = new Customer(Long.parseLong(idText), name);
    customerList.add(customer);
    logger.info("Thêm khách hàng: {}", name);
    return customer;
  }

  /**
   * Đọc một dòng mô tả tài khoản và thêm vào khách hàng hiện tại.
   *
   * @param current khách hàng đang đọc dở
   * @param line dòng mô tả tài khoản
   */
  private void addAccount(Customer current, String line) {
    if (current == null) {
      logger.warn("Bỏ qua dòng tài khoản vì chưa có khách hàng.");
      return;
    }
    String[] parts = line.split("\\s+");
    if (parts.length < MIN_ACCOUNT_PARTS) {
      logger.warn("Bỏ qua dòng tài khoản không đủ thông tin.");
      return;
    }
    long accountNumber = Long.parseLong(parts[0]);
    double balance = Double.parseDouble(parts[2]);
    Account account = createAccount(parts[1], accountNumber, balance);
    if (account != null) {
      current.addAccount(account);
      logger.info("Thêm tài khoản {} cho khách hàng {}.", accountNumber,
          current.getFullName());
    }
  }

  /**
   * Tạo tài khoản theo loại ghi trong file.
   *
   * @param typeText loại tài khoản (CHECKING hoặc SAVINGS)
   * @param accountNumber số tài khoản
   * @param balance số dư ban đầu
   * @return tài khoản vừa tạo, hoặc null nếu loại tài khoản không hợp lệ
   */
  private Account createAccount(String typeText, long accountNumber, double balance) {
    if (Account.CHECKING_TYPE.equals(typeText)) {
      return new CheckingAccount(accountNumber, balance);
    }
    if (Account.SAVINGS_TYPE.equals(typeText)) {
      return new SavingsAccount(accountNumber, balance);
    }
    logger.warn("Bỏ qua loại tài khoản không hợp lệ: {}", typeText);
    return null;
  }

  /**
   * Trả về thông tin khách hàng sắp xếp theo mã CMND tăng dần.
   *
   * @return chuỗi thông tin khách hàng
   */
  public String getCustomersInfoByIdOrder() {
    return buildCustomersInfo(Comparator.comparingLong(Customer::getIdNumber));
  }

  /**
   * Trả về thông tin khách hàng sắp xếp theo tên, trùng tên thì theo mã CMND.
   *
   * @return chuỗi thông tin khách hàng
   */
  public String getCustomersInfoByNameOrder() {
    return buildCustomersInfo(Comparator.comparing(Customer::getFullName)
        .thenComparingLong(Customer::getIdNumber));
  }

  /**
   * Sắp xếp khách hàng theo tiêu chí cho trước rồi nối thông tin thành chuỗi.
   *
   * @param comparator tiêu chí sắp xếp
   * @return chuỗi thông tin khách hàng đã sắp xếp
   */
  private String buildCustomersInfo(Comparator<Customer> comparator) {
    logger.debug("Đang tổng hợp thông tin khách hàng.");
    List<Customer> sorted = new ArrayList<>(customerList);
    sorted.sort(comparator);
    StringBuilder info = new StringBuilder();
    for (int i = 0; i < sorted.size(); i++) {
      info.append(sorted.get(i).getCustomerInfo());
      if (i < sorted.size() - 1) {
        info.append("\n");
      }
    }
    return info.toString();
  }
}
