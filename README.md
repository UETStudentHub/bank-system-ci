# Bài 2: Code Quality - Checkstyle (BankSystem)

## 1. Vì sao chọn Google Java Style

Google Java Style là chuẩn code phổ biến nhất trong giới Java: thụt lề 2 khoảng
trắng, dấu `{` mở cùng dòng, mọi khối `if`/`for`/`while` đều có ngoặc nhọn,
dòng tối đa 100 ký tự, Javadoc đầy đủ. Chuẩn này có sẵn trong Checkstyle
dưới dạng file `google_checks.xml` đi kèm plugin, nên không cần viết file
cấu hình riêng, cả nhóm dùng chung một chuẩn là xong.

## 2. Cấu hình Checkstyle trong pom.xml

Dùng `maven-checkstyle-plugin` phiên bản `3.3.1`:

- `configLocation` là `google_checks.xml` (file chuẩn Google đi kèm plugin).
- Execution `checkstyle` gắn vào phase `verify`, goal `check`: mỗi lần chạy
  `mvn clean verify`, Checkstyle tự kiểm tra toàn bộ code và làm fail build
  nếu còn vi phạm.
- `failsOnError=true`: có lỗi là build fail ngay, không cho qua.
- `consoleOutput=true`: in lỗi ra console để đọc luôn, không cần mở file báo cáo.

Chạy kiểm tra riêng (không cần build hết):

```bash
mvn checkstyle:check
```

Chạy kiểm tra kèm build đầy đủ:

```bash
mvn verify
```

## 3. Các nhóm vi phạm đã sửa

| Nhóm vi phạm | Ví dụ trong code gốc | Cách sửa |
| --- | --- | --- |
| Lỗi biên dịch | `Transaction.java` có một dòng bắt đầu bằng đúng một dấu `/` (không phải comment) | Viết lại thành Javadoc tử tế cho `getTypeString` |
| Thiếu package | 8/9 file không có `package`, chỉ `Account.java` có | Thêm `package bank_system;` cho cả 9 file (không thì class trong package không dùng được class ở default package) |
| Wildcard import, import thừa | `import java.util.*;`, `import java.io.*;` trong `Account.java`, `Bank.java` | Import cụ thể từng class, bỏ import không dùng; import xếp theo thứ tự bảng chữ cái (`java.*` trước, rồi `org.slf4j`) |
| Đặt tên sai chuẩn | `checking_type`, `_accNum`, `B`, `list`, `c_list`, `get_type_string`, biến `iB`/`fB`/`a` | Hằng số `CHECKING_TYPE`/`SAVINGS_TYPE` (UPPER_SNAKE_CASE); field `accountNumber`, `balance`, `transactionList`, `customerList`; method `getTypeString`; biến `initialBalance`/`finalBalance`/`amount` |
| Thụt lề, dấu ngoặc, dòng dài | `{` mở đầu dòng mới, hàm viết 1 dòng, `if` không có ngoặc nhọn, dòng dài hơn 200 ký tự | Thụt lề 2 spaces, `{` cùng dòng, mọi khối lệnh đều có ngoặc nhọn, mọi dòng ≤ 100 ký tự |
| Magic number / magic string | `3`, `4`, `1000.0`, `5000.0`, `1`–`4` trong switch, `"\\d{9}"`, `"CHECKING"`/`"SAVINGS"` | Hằng số `Transaction.TYPE_*`, `MAX_WITHDRAWAL_AMOUNT`, `MIN_BALANCE`, `ID_NUMBER_PATTERN`, `MIN_ACCOUNT_PARTS`; dùng `Account.CHECKING_TYPE`/`SAVINGS_TYPE` |
| Exception chung chung | `throws Exception`, `catch (Exception e)` | `doWithdrawing` ném `BankException`; catch `BankException` hoặc `IOException | NumberFormatException` |
| Nối chuỗi trong vòng lặp | `s += ...` trong `getTransactionHistory`, `res += ...` trong `Bank` | Dùng `StringBuilder` |
| Code trùng lặp, lồng if sâu | 2 hàm sắp xếp khách hàng trùng logic; `readCustomerList` lồng if 5 tầng | Gộp thành 1 hàm private chung `buildCustomersInfo(Comparator)`; tách `readCustomerList` thành các helper `processLine`, `addCustomer`, `addAccount`, `createAccount` |
| Dùng anonymous class | `new Comparator<Customer>() {...}` | Lambda / `Comparator.comparingLong`, `thenComparingLong` |
| In log ra console | `System.out.println`, `System.err.println` khắp nơi | Bỏ hết, dùng SLF4J `Logger` (xem mục 4) |
| Thiếu Javadoc | Hầu hết class và public method không có Javadoc | Thêm Javadoc cho mọi class, constructor, public method (kèm `@param`, `@return`, `@throws`) |

## 4. Logging: chọn mức log nào và ghi gì

Dùng SLF4J (`Logger`) + Logback. Mỗi class có một logger riêng:

```java
private static final Logger logger = LoggerFactory.getLogger(TenClass.class);
```

| Mức | Dùng khi nào | Ghi những gì |
| --- | --- | --- |
| INFO | Sự kiện nghiệp vụ thành công, cần nhìn thấy khi chạy | Nạp/rút tiền thành công (số tài khoản + số tiền + số dư đầu → cuối); thêm khách hàng (họ tên); thêm tài khoản (số tài khoản + tên khách hàng) |
| DEBUG | Chi tiết luồng chạy, chỉ cần khi debug | Bắt đầu đọc file, đang xử lý từng dòng, đang tổng hợp thông tin/summary, đang xử lý giao dịch, thêm/xóa tài khoản |
| WARN | Vấn đề hồi phục được, chương trình vẫn chạy tiếp | Thêm tài khoản đã tồn tại (trùng); loại tài khoản lạ trong file; dòng trong file sai định dạng hoặc thiếu thông tin; luồng đầu vào là null |
| ERROR | Lỗi, kèm exception để có stack trace | Nạp/rút tiền thất bại; lỗi đọc file (`IOException`, `NumberFormatException`) |

Không log dữ liệu nhạy cảm: số CMND không bao giờ xuất hiện trong log
(chỉ log họ tên và số tài khoản, là dữ liệu nghiệp vụ cần thiết).

File `src/main/resources/logback.xml` cấu hình console appender, mỗi dòng log
có timestamp, mức log, tên class và nội dung; mức mặc định của root logger
là INFO (muốn thấy cả DEBUG thì đổi `level="INFO"` thành `level="DEBUG"`).

## 5. Cách chạy

```bash
mvn checkstyle:check   # chỉ kiểm tra code style
mvn clean verify       # build đầy đủ (biên dịch + checkstyle ở phase verify)
```

