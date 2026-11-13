package bank_system;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/** Test cho lớp Transaction và thông báo của các exception. */
class TransactionTest {

  @Test
  void getTypeStringTraVeDungTen() {
    Assertions.assertEquals("Nạp tiền vãng lai",
        Transaction.getTypeString(Transaction.TYPE_DEPOSIT_CHECKING));
    Assertions.assertEquals("Rút tiền vãng lai",
        Transaction.getTypeString(Transaction.TYPE_WITHDRAW_CHECKING));
    Assertions.assertEquals("Nạp tiền tiết kiệm",
        Transaction.getTypeString(Transaction.TYPE_DEPOSIT_SAVINGS));
    Assertions.assertEquals("Rút tiền tiết kiệm",
        Transaction.getTypeString(Transaction.TYPE_WITHDRAW_SAVINGS));
    Assertions.assertEquals("Không rõ", Transaction.getTypeString(99));
  }

  @Test
  void getTransactionSummaryChuaDuThongTin() {
    Transaction transaction = new Transaction(
        Transaction.TYPE_DEPOSIT_CHECKING, 500, 1000, 1500);
    String summary = transaction.getTransactionSummary();
    Assertions.assertTrue(summary.contains("Nạp tiền vãng lai"));
    Assertions.assertTrue(summary.contains("$1000.00"));
    Assertions.assertTrue(summary.contains("$500.00"));
    Assertions.assertTrue(summary.contains("$1500.00"));
  }

  @Test
  void thongBaoNgoaiLeChuaSoTien() {
    InsufficientFundsException notEnough = new InsufficientFundsException(200);
    Assertions.assertTrue(notEnough.getMessage().contains("200.00"));
    InvalidFundingAmountException invalid = new InvalidFundingAmountException(-5);
    Assertions.assertTrue(invalid.getMessage().contains("-5.00"));
  }
}
