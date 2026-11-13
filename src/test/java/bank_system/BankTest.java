package bank_system;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test cho lớp Bank: đọc danh sách khách hàng từ dữ liệu text và sắp xếp
 * thông tin khách hàng.
 *
 * <p>Định dạng một dòng khách hàng: họ tên rồi số CMND 9 chữ số.
 * Định dạng một dòng tài khoản: số tài khoản, loại, số dư.
 */
class BankTest {

  @Test
  void docDanhSachKhachHangVaTaiKhoan() {
    Bank bank = new Bank();
    String data = "Nguyen Van A 111111111\n"
        + "1001 CHECKING 1000\n"
        + "1002 SAVINGS 6000\n";
    bank.readCustomerList(new ByteArrayInputStream(
        data.getBytes(StandardCharsets.UTF_8)));
    Assertions.assertEquals(1, bank.getCustomerList().size());
    Customer customer = bank.getCustomerList().get(0);
    Assertions.assertEquals("Nguyen Van A", customer.getFullName());
    Assertions.assertEquals(2, customer.getAccountList().size());
    Assertions.assertEquals(1000, customer.getAccountList().get(0).getBalance(),
        0.001);
    Assertions.assertTrue(
        customer.getAccountList().get(1) instanceof SavingsAccount);
    Assertions.assertEquals(6000, customer.getAccountList().get(1).getBalance(),
        0.001);
  }

  @Test
  void docDanhSachNullKhongLoi() {
    Bank bank = new Bank();
    bank.readCustomerList(null);
    Assertions.assertEquals(0, bank.getCustomerList().size());
  }

  @Test
  void layThongTinKhachHangSapXepTheoCmnd() {
    Bank bank = new Bank();
    String data = "Tran Van B 222222222\n"
        + "2001 CHECKING 1000\n"
        + "Nguyen Van A 111111111\n"
        + "1001 CHECKING 1000\n";
    bank.readCustomerList(new ByteArrayInputStream(
        data.getBytes(StandardCharsets.UTF_8)));
    String info = bank.getCustomersInfoByIdOrder();
    Assertions.assertTrue(info.indexOf("111111111") < info.indexOf("222222222"));
    Assertions.assertTrue(info.contains("Nguyen Van A"));
    Assertions.assertTrue(info.contains("Tran Van B"));
  }
}
