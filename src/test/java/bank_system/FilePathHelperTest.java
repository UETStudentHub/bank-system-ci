package bank_system;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test cho FilePathHelper.
 *
 * <p>Test này bắt được lỗi nối đường dẫn bằng dấu cứng.
 * Trên Linux và macOS, dấu gạch chéo ngược không phải là dấu phân cách,
 * "data\customers.txt" chỉ là một tên file duy nhất,
 * getNameCount() bằng 1 thay vì 2.
 * Trên Windows dấu gạch chéo ngược là dấu phân cách nên getNameCount() bằng 2.
 * Vì vậy test chỉ đậu trên mọi hệ điều hành khi joinPath dùng Paths.get.
 */
class FilePathHelperTest {

  @Test
  void joinPathTraVeDungDuongDan() {
    Path path = Paths.get(FilePathHelper.joinPath("data", "customers.txt"));
    Assertions.assertEquals(2, path.getNameCount());
    Assertions.assertEquals("customers.txt", path.getFileName().toString());
  }

  @Test
  void joinPathChuaDauPhanCachCuaHeDieuHanh() {
    String joined = FilePathHelper.joinPath("data", "customers.txt");
    Assertions.assertTrue(joined.contains(File.separator));
  }
}
