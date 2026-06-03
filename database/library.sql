-- ============================================================
-- Script tạo database cho hệ thống Thư viện Quốc gia
-- Modul Quản lí nhân viên  (v2 – có phân quyền)
-- ============================================================

CREATE DATABASE IF NOT EXISTS library
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE library;

-- ============================================================
-- Bảng tblStaff (nhân viên) – tạo trước vì tblAccount tham chiếu
-- ============================================================
CREATE TABLE IF NOT EXISTS tblStaff (
    id        INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    fullname  VARCHAR(100) NOT NULL,
    role      VARCHAR(50)  NOT NULL,   -- 'Manager' | 'Employee' | 'Admin'
    tel       VARCHAR(15),
    email     VARCHAR(100)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- Bảng tblAccount (tài khoản đăng nhập)
-- role: 'Admin' | 'Manager' | 'Employee'
-- staffId: liên kết 1-1 với tblStaff (NULL nếu tài khoản hệ thống)
-- ============================================================
CREATE TABLE IF NOT EXISTS tblAccount (
    idAcc      VARCHAR(20)  NOT NULL PRIMARY KEY,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    password   VARCHAR(100) NOT NULL,
    role       VARCHAR(20)  NOT NULL DEFAULT 'Employee',  -- phân quyền
    status     VARCHAR(20)  NOT NULL DEFAULT 'active',
    staffId    INT          NULL,
    createDate DATETIME     DEFAULT CURRENT_TIMESTAMP,
    updateDate DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (staffId) REFERENCES tblStaff(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- Dữ liệu mẫu – tblStaff
-- ============================================================
INSERT INTO tblStaff (id, fullname, role, tel, email) VALUES
(1, 'Nguyen Van An',   'Admin',    '0901234567', 'an.nguyen@lib.vn'),
(2, 'Tran Thi Binh',   'Manager',  '0912345678', 'binh.tran@lib.vn'),
(3, 'Le Van Cuong',    'Employee', '0923456789', 'cuong.le@lib.vn'),
(4, 'Pham Thi Dung',   'Employee', '0934567890', 'dung.pham@lib.vn'),
(5, 'Hoang Van Em',    'Employee', '0945678901', 'em.hoang@lib.vn');

-- ============================================================
-- Dữ liệu mẫu – tblAccount  (password: 123456)
-- ============================================================
INSERT INTO tblAccount (idAcc, username, password, role, status, staffId) VALUES
('ACC001', 'admin',   '123456', 'Admin',    'active', 1),
('ACC002', 'manager', '123456', 'Manager',  'active', 2),
('ACC003', 'cuong',   '123456', 'Employee', 'active', 3),
('ACC004', 'dung',    '123456', 'Employee', 'active', 4),
('ACC005', 'em',      '123456', 'Employee', 'active', 5);
