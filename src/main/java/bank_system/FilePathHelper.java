package bank_system;

import java.nio.file.Paths;

public class FilePathHelper {

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
