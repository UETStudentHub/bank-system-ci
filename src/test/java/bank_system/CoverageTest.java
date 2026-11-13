package bank_system;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test bổ sung để phủ các nhánh code còn trống: setter, equals, lịch sử
 * giao dịch, sắp xếp theo tên, đọc file có dòng lỗi, nạp tiền tiết kiệm.
 */
class CoverageTest {

  @Test
  void accountSettersVaLichSuGiaoDich() {
    CheckingAccount account = new CheckingAccount(1001, 1000);
    account.setAccountNumber(2001);
    Assertions.assertEquals(2001, account.getAccountNumber());
    account.setTransactionList(null);
    Assertions.assertEquals(0, account.getTransactionList().size());
    Transaction first = new Transaction(
        Transaction.TYPE_DEPOSIT_CHECKING, 500, 1000, 1500);
    account.setTransactionList(List.of(first));
    Assertions.assertEquals(1, account.getTransactionList().size());
    account.addTransaction(null);
    Assertions.assertEquals(1, account.getTransactionList().size());
    CheckingAccount empty = new CheckingAccount(1001, 1000);
    Assertions.assertTrue(empty.getTransactionHistory().contains("1001"));
    empty.deposit(500);
    empty.withdraw(200);
    String history = empty.getTransactionHistory();
    Assertions.assertTrue(history.contains("Nạp tiền vãng lai"));
    Assertions.assertTrue(history.contains("Rút tiền vãng lai"));
  }

  @Test
  void accountEqualsVaHashCode() {
    CheckingAccount account = new CheckingAccount(1001, 1000);
    Assertions.assertTrue(account.equals(account));
    Assertions.assertFalse(account.equals(null));
    Assertions.assertFalse(account.equals("khong phai tai khoan"));
    Assertions.assertTrue(account.equals(new CheckingAccount(1001, 500)));
    Assertions.assertFalse(account.equals(new CheckingAccount(1002, 1000)));
    Assertions.assertEquals(new CheckingAccount(1001, 500).hashCode(),
        account.hashCode());
  }

  @Test
  void doWithdrawingSoTienAmNemNgoaiLe() {
    CheckingAccount account = new CheckingAccount(1001, 1000);
    Assertions.assertThrows(InvalidFundingAmountException.class,
        () -> account.doWithdrawing(-5));
  }

  @Test
  void tietKiemNapTien() {
    SavingsAccount account = new SavingsAccount(2001, 6000);
    account.deposit(500);
    Assertions.assertEquals(6500, account.getBalance(), 0.001);
    Assertions.assertEquals(1, account.getTransactionList().size());
    Assertions.assertEquals(Transaction.TYPE_DEPOSIT_SAVINGS,
        account.getTransactionList().get(0).getType());
    account.deposit(-100);
    Assertions.assertEquals(6500, account.getBalance(), 0.001);
    Assertions.assertEquals(1, account.getTransactionList().size());
  }

  @Test
  void tietKiemRutSoTienAmKhongDoiSoDu() {
    SavingsAccount account = new SavingsAccount(2001, 6000);
    account.withdraw(-100);
    Assertions.assertEquals(6000, account.getBalance(), 0.001);
    Assertions.assertEquals(0, account.getTransactionList().size());
  }

  @Test
  void transactionSettersVaGetters() {
    Transaction transaction = new Transaction(
        Transaction.TYPE_DEPOSIT_CHECKING, 500, 1000, 1500);
    transaction.setType(Transaction.TYPE_WITHDRAW_CHECKING);
    transaction.setAmount(300);
    transaction.setInitialBalance(1000);
    transaction.setFinalBalance(700);
    Assertions.assertEquals(Transaction.TYPE_WITHDRAW_CHECKING,
        transaction.getType());
    Assertions.assertEquals(300, transaction.getAmount(), 0.001);
    Assertions.assertEquals(1000, transaction.getInitialBalance(), 0.001);
    Assertions.assertEquals(700, transaction.getFinalBalance(), 0.001);
  }

  @Test
  void customerSettersVaMacDinh() {
    Customer customer = new Customer();
    Assertions.assertEquals(0, customer.getIdNumber());
    Assertions.assertEquals("", customer.getFullName());
    customer.setIdNumber(111111111L);
    customer.setFullName("Nguyen Van A");
    Assertions.assertEquals(111111111L, customer.getIdNumber());
    Assertions.assertEquals("Nguyen Van A", customer.getFullName());
    customer.setAccountList(null);
    Assertions.assertEquals(0, customer.getAccountList().size());
    List<Account> list = new ArrayList<>();
    list.add(new CheckingAccount(1001, 1000));
    customer.setAccountList(list);
    Assertions.assertEquals(1, customer.getAccountList().size());
  }

  @Test
  void customerThemVaXoaTaiKhoan() {
    Customer customer = new Customer(111111111L, "Nguyen Van A");
    customer.addAccount(null);
    Assertions.assertEquals(0, customer.getAccountList().size());
    CheckingAccount first = new CheckingAccount(1001, 1000);
    customer.addAccount(first);
    customer.addAccount(new CheckingAccount(1001, 2000));
    Assertions.assertEquals(1, customer.getAccountList().size());
    customer.removeAccount(null);
    Assertions.assertEquals(1, customer.getAccountList().size());
    customer.removeAccount(first);
    Assertions.assertEquals(0, customer.getAccountList().size());
  }

  @Test
  void bankSetCustomerList() {
    Bank bank = new Bank();
    bank.setCustomerList(null);
    Assertions.assertEquals(0, bank.getCustomerList().size());
    List<Customer> list = new ArrayList<>();
    list.add(new Customer(111111111L, "Nguyen Van A"));
    bank.setCustomerList(list);
    Assertions.assertEquals(1, bank.getCustomerList().size());
  }

  @Test
  void bankSapXepTheoTen() {
    Bank bank = new Bank();
    String data = "Tran Van B 222222222\n"
        + "2001 CHECKING 1000\n"
        + "Nguyen Van A 333333333\n"
        + "3001 CHECKING 1000\n"
        + "Nguyen Van A 111111111\n"
        + "1001 CHECKING 1000\n";
    bank.readCustomerList(new ByteArrayInputStream(
        data.getBytes(StandardCharsets.UTF_8)));
    String info = bank.getCustomersInfoByNameOrder();
    int first = info.indexOf("111111111");
    int second = info.indexOf("333333333");
    int third = info.indexOf("222222222");
    Assertions.assertTrue(first < second);
    Assertions.assertTrue(second < third);
  }

  @Test
  void bankBoQuaDongLoi() {
    Bank bank = new Bank();
    String data = "\nhello\n1001 CHECKING 1000\n"
        + "Nguyen Van A 111111111\n"
        + "1001 CHECKING\n"
        + "1002 UNKNOWN 1000\n"
        + "1003 CHECKING 2000\n\n";
    bank.readCustomerList(new ByteArrayInputStream(
        data.getBytes(StandardCharsets.UTF_8)));
    Assertions.assertEquals(1, bank.getCustomerList().size());
    Customer customer = bank.getCustomerList().get(0);
    Assertions.assertEquals(1, customer.getAccountList().size());
    Assertions.assertEquals(1003,
        customer.getAccountList().get(0).getAccountNumber());
    Assertions.assertEquals(2000,
        customer.getAccountList().get(0).getBalance(), 0.001);
  }

  @Test
  void bankDocDongSoDuKhongPhaiSo() {
    Bank bank = new Bank();
    String data = "Nguyen Van A 111111111\n1001 CHECKING abc\n";
    bank.readCustomerList(new ByteArrayInputStream(
        data.getBytes(StandardCharsets.UTF_8)));
    Assertions.assertEquals(1, bank.getCustomerList().size());
    Assertions.assertEquals(0,
        bank.getCustomerList().get(0).getAccountList().size());
  }

  @Test
  void bankExceptionTrucTiep() {
    BankException exception = new BankException("Lỗi đọc file");
    Assertions.assertEquals("Lỗi đọc file", exception.getMessage());
  }
}
