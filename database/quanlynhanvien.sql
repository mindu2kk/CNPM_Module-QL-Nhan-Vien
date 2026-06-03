-- ============================================
-- Script tạo database Quản lý nhân viên
-- ============================================

CREATE DATABASE IF NOT EXISTS quanlynhanvien
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE quanlynhanvien;

-- ============================================
-- Bảng account (tài khoản đăng nhập)
-- ============================================
CREATE TABLE IF NOT EXISTS account (
    idAcc       VARCHAR(20)  NOT NULL PRIMARY KEY,
    username    VARCHAR(50)  NOT NULL UNIQUE,
    password    VARCHAR(100) NOT NULL,
    status      VARCHAR(20)  NOT NULL DEFAULT 'active',
    createDate  DATETIME     DEFAULT CURRENT_TIMESTAMP,
    updateDate  DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- Bảng staff (nhân viên)
-- ============================================
CREATE TABLE IF NOT EXISTS staff (
    ID          INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    fullname    VARCHAR(100) NOT NULL,
    role        VARCHAR(50)  NOT NULL,
    tel         VARCHAR(15),
    email       VARCHAR(100)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- Dữ liệu mẫu - Tài khoản
-- ============================================
INSERT INTO account (idAcc, username, password, status) VALUES
('ACC001', 'admin',    '123456', 'active'),
('ACC002', 'manager',  '123456', 'active'),
('ACC003', 'user01',   '123456', 'active');

-- ============================================
-- Dữ liệu mẫu - Nhân viên
-- ============================================
INSERT INTO staff (fullname, role, tel, email) VALUES
('Nguyễn Văn An',    'Giám đốc',          '0901234567', 'an.nguyen@company.com'),
('Trần Thị Bình',    'Trưởng phòng HR',   '0912345678', 'binh.tran@company.com'),
('Lê Văn Cường',     'Lập trình viên',    '0923456789', 'cuong.le@company.com'),
('Phạm Thị Dung',    'Kế toán',           '0934567890', 'dung.pham@company.com'),
('Hoàng Văn Em',     'Nhân viên kinh doanh', '0945678901', 'em.hoang@company.com'),
('Vũ Thị Phương',    'Thiết kế đồ họa',   '0956789012', 'phuong.vu@company.com'),
('Đặng Văn Giang',   'Quản trị mạng',     '0967890123', 'giang.dang@company.com'),
('Bùi Thị Hoa',      'Nhân viên hành chính', '0978901234', 'hoa.bui@company.com');
