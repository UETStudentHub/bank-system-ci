package bank_system;

import java.nio.file.Paths;

/**
 * Ghép tên thư mục và tên file thành một đường dẫn.
 */
public class FilePathHelper {

  /** Không cho tạo đối tượng, lớp này chỉ có hàm static. */
  private FilePathHelper() {
  }

  /**
   * Ghép thư mục và tên file thành đường dẫn của hệ điều hành đang chạy.
   *
   * @param folder tên thư mục
   * @param fileName tên file
   * @return đường dẫn đã ghép
   */
  public static String joinPath(String folder, String fileName) {
    return Paths.get(folder, fileName).toString();
  }
}
