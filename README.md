# Modul Quản lí Nhân viên – Thư viện Quốc gia

Ứng dụng Java Swing theo mô hình MVC, kết nối MySQL.  
Tương ứng với **Chương 9 – Pha Cài đặt và Tích hợp** trong tài liệu môn CNPM.

---

## Cấu trúc project

```
QuanLyNhanVien/
├── src/
│   ├── model/
│   │   ├── Account.java
│   │   └── Staff.java
│   ├── dao/
│   │   ├── DAO.java            ← kết nối MySQL (base class)
│   │   ├── AccountDAO.java     ← checkLogin()
│   │   └── StaffDAO.java       ← searchStaff, addStaff, updateStaff, deleteStaff
│   ├── view/
│   │   ├── login/
│   │   │   └── LoginFrm.java         ← điểm khởi chạy (có main)
│   │   └── staff/
│   │       ├── ManageHomeFrm.java
│   │       ├── ManageStaffFrm.java
│   │       ├── SearchStaffFrm.java
│   │       └── EditStaffFrm.java
│   └── test/unit/
│       ├── AccountDAOTest.java   ← JUnit 4
│       └── StaffDAOTest.java     ← JUnit 4
└── database/
    └── library.sql               ← tạo DB + dữ liệu mẫu
```

---

## Yêu cầu

| Thành phần | Phiên bản |
|---|---|
| JDK | 8+ |
| MySQL | 5.7 / 8.0 (port **3307**) |
| mysql-connector-java | 5.x (driver `com.mysql.jdbc.Driver`) |
| JUnit | 4.x |

---

## Cài đặt

### 1. Tạo database

```sql
source database/library.sql
```

### 2. Cấu hình kết nối

Mở `src/dao/DAO.java`, chỉnh lại nếu cần:

```java
String dbUrl = "jdbc:mysql://localhost:3307/library?autoReconnect=true&useSSL=false";
// user: root  |  password: xxx
```

### 3. Thêm thư viện vào Build Path

- `mysql-connector-java-x.x.x.jar`
- `junit-4.x.jar` + `hamcrest-core.jar`

### 4. Chạy ứng dụng

Chạy class `view.login.LoginFrm` (có hàm `main`).

### 5. Chạy JUnit tests

Chuột phải vào package `test.unit` → **Run As → JUnit Test**.

---

## Tài khoản mẫu

| Username | Password | Status |
|---|---|---|
| admin   | 123456 | active |
| manager | 123456 | active |
| user01  | 123456 | active |

---

## Luồng tích hợp (theo tài liệu §9.5.3)

```
LoginFrm
  └─(đăng nhập thành công)→ ManageHomeFrm
       └─(click "Quản lí nhân viên")→ ManageStaffFrm
            └─(click "Sửa thông tin nhân viên")→ SearchStaffFrm
                 └─(click chọn dòng)→ EditStaffFrm → (lưu) → StaffDAO.updateStaff()
```

---

## Các test case JUnit

### AccountDAOTest

| Test | Mô tả | Kỳ vọng |
|---|---|---|
| testCheckLogin_Success | Đúng username + password | `true`, idAcc != null |
| testCheckLogin_WrongPassword | Sai password | `false` |
| testCheckLogin_UserNotExist | Username không tồn tại | `false` |
| testCheckLogin_EmptyUsername | Username rỗng | `false` |
| testCheckLogin_EmptyPassword | Password rỗng | `false` |

### StaffDAOTest

| Test | Mô tả | Kỳ vọng |
|---|---|---|
| test1_SearchStaff_ByName_Found | Tìm "Nguyen" | length > 0 |
| test2_SearchStaff_NotFound | Tìm chuỗi không tồn tại | length == 0 |
| test3_SearchStaff_EmptyKey_ReturnsAll | Từ khóa rỗng | length >= 0 |
| test4_AddStaff_Success | Thêm nhân viên mới | `true` |
| test5_AddStaff_CanBeFoundAfterAdd | Tìm lại sau khi thêm | tìm thấy |
| test6_UpdateStaff_Success | Cập nhật SĐT | `true`, SĐT thay đổi |
| test7_UpdateStaff_IdNotExist | id không tồn tại | `false` |
| test8_DeleteStaff_Success | Xóa nhân viên test | `true` |
| test9_DeleteStaff_NotFoundAfterDelete | Tìm lại sau khi xóa | length == 0 |
| test10_DeleteStaff_IdNotExist | id không tồn tại | `false` |
