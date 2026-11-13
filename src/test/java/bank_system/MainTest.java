package bank_system;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/** Test cho lớp Main: hàm main chạy hết demo mà không lỗi. */
class MainTest {

  @Test
  void mainChayDemoKhongLoi() {
    Assertions.assertDoesNotThrow(() -> Main.main(new String[0]));
  }
}
