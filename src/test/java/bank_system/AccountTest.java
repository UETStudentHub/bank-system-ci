package bank_system;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test cho tài khoản vãng lai và tài khoản tiết kiệm.
 *
 * <p>Lưu ý: hai hàm public deposit và withdraw bắt exception trong nội bộ
 * rồi chỉ ghi log, nên khi nạp/rút sai thì số dư giữ nguyên và không có
 * exception nào ném ra ngoài. Muốn kiểm tra exception thì gọi trực tiếp
 * hai hàm protected doDepositing và doWithdrawing (test nằm cùng package
 * nên gọi được).
 */
class AccountTest {

  @Test
  void depositCheckingTangSoDu() {
    CheckingAccount account = new CheckingAccount(1001, 1000);
    account.deposit(500);
    Assertions.assertEquals(1500, account.getBalance(), 0.001);
    Assertions.assertEquals(1, account.getTransactionList().size());
    Assertions.assertEquals(Transaction.TYPE_DEPOSIT_CHECKING,
        account.getTransactionList().get(0).getType());
  }

  @Test
  void withdrawCheckingGiamSoDu() {
    CheckingAccount account = new CheckingAccount(1001, 1000);
    account.withdraw(200);
    Assertions.assertEquals(800, account.getBalance(), 0.001);
    Assertions.assertEquals(1, account.getTransactionList().size());
    Assertions.assertEquals(Transaction.TYPE_WITHDRAW_CHECKING,
        account.getTransactionList().get(0).getType());
  }

  @Test
  void rutVuotSoDuVaNapTienKhongHopLeKhongDoiSoDu() {
    CheckingAccount account = new CheckingAccount(1001, 1000);
    account.withdraw(2000);
    Assertions.assertEquals(1000, account.getBalance(), 0.001);
    account.deposit(-50);
    Assertions.assertEquals(1000, account.getBalance(), 0.001);
    account.deposit(0);
    Assertions.assertEquals(1000, account.getBalance(), 0.001);
    Assertions.assertEquals(0, account.getTransactionList().size());
  }

  @Test
  void tietKiemRutTienHopLe() {
    SavingsAccount account = new SavingsAccount(2001, 10000);
    account.withdraw(500);
    Assertions.assertEquals(9500, account.getBalance(), 0.001);
    Assertions.assertEquals(1, account.getTransactionList().size());
  }

  @Test
  void tietKiemRutVuotGioiHanKhongDoiSoDu() {
    SavingsAccount overMax = new SavingsAccount(2001, 10000);
    overMax.withdraw(2000);
    Assertions.assertEquals(10000, overMax.getBalance(), 0.001);
    SavingsAccount belowMin = new SavingsAccount(2002, 5500);
    belowMin.withdraw(1000);
    Assertions.assertEquals(5500, belowMin.getBalance(), 0.001);
  }

  @Test
  void doDepositingVaDoWithdrawingNemNgoaiLe() {
    CheckingAccount account = new CheckingAccount(1001, 1000);
    Assertions.assertThrows(InvalidFundingAmountException.class,
        () -> account.doDepositing(-10));
    Assertions.assertThrows(InsufficientFundsException.class,
        () -> account.doWithdrawing(2000));
  }
}
