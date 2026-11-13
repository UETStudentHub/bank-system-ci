# Bài 3: CI/CD automation (BankSystem)

Bài này dùng lại hệ thống BankSystem của Bài 2, chỉ thêm 2 thứ: Unit Tests
(JUnit 5) và workflow GitHub Actions. Code chính trong `src/main/java`
giữ nguyên như Bài 2, không sửa gì.

## 1. Trong project có gì mới so với Bài 2

- `pom.xml`: thêm `junit-jupiter 5.9.2` (scope `test`, cùng version đã
  dùng ở Bài 1) và `maven-surefire-plugin 3.0.0` để chạy test. Java vẫn 11,
  Checkstyle vẫn như cũ, groupId/artifactId không đổi
  (`com.practice.maven` / `bank-system` / `1.0-SNAPSHOT`).
- `src/test/java/bank_system/` — 12 test, chia 3 file:
  - `AccountTest.java` (6 test): nạp/rút tài khoản vãng lai, rút vượt số dư
    và nạp tiền âm thì số dư không đổi, rút tiền tiết kiệm hợp lệ, rút tiết
    kiệm vượt hạn mức 1000 hoặc làm số dư dưới 5000 thì không đổi, gọi trực
    tiếp `doDepositing`/`doWithdrawing` thì ném đúng exception.
  - `BankTest.java` (3 test): đọc danh sách khách hàng và tài khoản từ dữ
    liệu text, đọc dữ liệu null thì không lỗi, sắp xếp thông tin khách hàng
    theo CMND tăng dần.
  - `TransactionTest.java` (3 test): `getTypeString` trả đúng tên 4 loại và
    "Không rõ" với mã lạ, tóm tắt giao dịch chứa đủ loại và số tiền, thông
    báo exception chứa đúng số tiền.
- `.github/workflows/ci.yml`: workflow CI, xem mục 2.
- `GIAI-THICH.md`: file giải thích của Bài 2, giữ nguyên.

## 2. Workflow làm gì, từng bước một

File `.github/workflows/ci.yml`, chạy khi có `push` hoặc `pull_request`
(trên mọi branch):

1. `actions/checkout@v4`: tải code của repo về máy chạy CI.
2. `actions/setup-java@v4`: cài JDK 11 (Temurin), bật cache Maven cho
   chạy nhanh lần sau.
3. `mvn -B verify`: chạy toàn bộ vòng đời Maven — biên dịch code, chạy
   test (surefire), đóng gói ra file `.jar` (phase `package`), rồi chạy
   Checkstyle (phase `verify`). Bước nào lỗi là cả workflow đỏ.
4. `actions/upload-artifact@v4`: lưu file `target/*.jar` lại với tên
   `bank-system-jar`, để tải về kiểm tra sau khi build xong.

## 3. Chạy thử ở máy mình trước

Đứng ở thư mục gốc của project (chỗ có file `pom.xml`), chạy:

```bash
mvn clean verify
```

Nếu thấy `Tests run: 12, Failures: 0, Errors: 0` và `BUILD SUCCESS` là
xong. Lệnh này chạy cả test, package và checkstyle trong một lần.

Lưu ý 2 cảnh báo vô hại, đã gặp ở Bài 2: Checkstyle in 2 dòng
`[WARN] ... 'bank_system' must match pattern ... [PackageName]` vì tên
package có dấu gạch dưới, nhưng kết quả cuối vẫn là
`You have 0 Checkstyle violations` và `BUILD SUCCESS`. Đừng đổi tên
package, đừng cố sửa 2 dòng đó.

Nếu `mvn clean verify` báo lỗi không xóa được thư mục `target` (giống
Bài 2), chạy `mvn verify` (bỏ chữ `clean`) là được.

## 4. Đẩy lên GitHub và xem CI chạy

1. Tạo repo mới trên GitHub (tên ví dụ `bank-system-ci`), chưa cần thêm
   file gì trong đó.
2. Ở máy mình, trong thư mục project, chạy:
   - `git init`
   - `git add .`
   - `git commit -m "feat: add JUnit tests and GitHub Actions CI"`
   - `git branch -M main`
   - `git remote add origin <link repo vừa tạo>`
   - `git push -u origin main`
3. Vào repo trên GitHub, bấm tab **Actions**: sẽ thấy một lần chạy tên
   `CI`, bấm vào xem từng bước xanh hay đỏ.
4. Khi lần chạy xanh, bấm vào lần chạy đó, kéo xuống cuối trang sẽ thấy
   mục **Artifacts** có `bank-system-jar`, bấm vào để tải file `.jar`
   về. File này nằm trong `target/bank-system-1.0-SNAPSHOT.jar` ở máy
   mình sau khi chạy `mvn verify`.

## 5. Cố tình gây lỗi build để demo debug

Đề bài yêu cầu chứng minh khả năng debug bằng log của GitHub Actions.
Làm như sau, từng bước một:

1. Mở file `src/test/java/bank_system/AccountTest.java`, sửa một giá trị
   kỳ vọng cho sai, ví dụ trong test nạp tiền sửa `1500` thành `1600`.
2. Commit và push lên GitHub như mục 4.
3. Vào tab **Actions**, lần chạy mới sẽ đỏ (fail). Bấm vào lần chạy đỏ,
   bấm vào job `build`, bấm vào bước `Build with Maven` để xem log.
4. Trong log sẽ thấy dòng test lỗi, ví dụ
   `AccountTest.depositCheckingTangSoDu: expected: <1500.0> but was: <1000.0>`
   hoặc `Tests run: 12, Failures: 1`, và cuối log có `BUILD FAILURE`.
   Đó chính là chỗ cần đọc để biết test nào sai và sai ở đâu.
5. Sửa lại `1600` thành `1500`, commit và push lại. Lần chạy mới sẽ
   xanh trở lại. Chụp lại 2 ảnh (lần đỏ + lần xanh) để nộp kèm là đủ
   chứng minh phần demo debug.

## 6. Chưa kiểm chứng được

Máy làm file này không có JDK và Maven nên chưa chạy thử `mvn verify`
được. Toàn bộ test, pom và workflow đã được đọc lại và đối chiếu từng
tên method/field với code chính, nhưng kết quả chạy thật chỉ biết chắc
khi Huyền chạy ở máy mình hoặc khi GitHub Actions chạy lần đầu. Nếu
Actions báo đỏ, chụp log gửi lại để sửa.
