-- ============================================================
-- Database: library (Thư viện Quốc gia - Module Quản lý nhân viên)
-- ============================================================

CREATE DATABASE IF NOT EXISTS library
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE library;

-- ============================================================
-- Bảng tblAccount
-- ============================================================
CREATE TABLE IF NOT EXISTS tblAccount (
    idAcc      VARCHAR(20)  NOT NULL PRIMARY KEY,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    password   VARCHAR(100) NOT NULL,
    role       VARCHAR(20)  NOT NULL DEFAULT 'Employee',  -- Manager | Admin | Employee
    status     VARCHAR(20)  NOT NULL DEFAULT 'active',
    createDate DATETIME     DEFAULT CURRENT_TIMESTAMP,
    updateDate DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- Bảng tblStaff
-- ============================================================
CREATE TABLE IF NOT EXISTS tblStaff (
    id       INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    fullname VARCHAR(100) NOT NULL,
    role     VARCHAR(50)  NOT NULL,
    tel      VARCHAR(15),
    email    VARCHAR(100)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- Dữ liệu mẫu – tblAccount
-- ============================================================
INSERT INTO tblAccount (idAcc, username, password, role, status) VALUES
('ACC001', 'admin',   '123456', 'Admin',    'active'),
('ACC002', 'manager', '123456', 'Manager',  'active'),
('ACC003', 'user01',  '123456', 'Employee', 'active');

-- ============================================================
-- Dữ liệu mẫu – tblStaff
-- ============================================================
INSERT INTO tblStaff (fullname, role, tel, email) VALUES
('Nguyen Van An',   'Manager',   '0901234567', 'an.nguyen@lib.vn'),
('Tran Thi Binh',   'Librarian', '0912345678', 'binh.tran@lib.vn'),
('Le Van Cuong',    'Librarian', '0923456789', 'cuong.le@lib.vn'),
('Pham Thi Dung',   'Admin',     '0934567890', 'dung.pham@lib.vn'),
('Hoang Van Em',    'Librarian', '0945678901', 'em.hoang@lib.vn');
