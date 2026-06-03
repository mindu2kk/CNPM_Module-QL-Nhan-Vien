-- ============================================================
-- Database: library  (Module Quản lý nhân viên – RBAC schema)
-- ============================================================

CREATE DATABASE IF NOT EXISTS library
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE library;

-- ============================================================
-- 1. tblStaff
-- ============================================================
CREATE TABLE IF NOT EXISTS tblStaff (
    ID       INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    fullname VARCHAR(255) NOT NULL,
    role     VARCHAR(255) NOT NULL,
    tel      VARCHAR(255),
    email    VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- 2. tblRole
-- ============================================================
CREATE TABLE IF NOT EXISTS tblRole (
    idRole      VARCHAR(255) NOT NULL PRIMARY KEY,
    roleName    VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- 3. Permission
-- ============================================================
CREATE TABLE IF NOT EXISTS Permission (
    idPermission   INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    permissionName VARCHAR(255) NOT NULL UNIQUE,
    description    VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- 4. tblRole_Permission  (N-N: Role ↔ Permission)
-- ============================================================
CREATE TABLE IF NOT EXISTS tblRole_Permission (
    id           VARCHAR(255) NOT NULL PRIMARY KEY,
    idRole       VARCHAR(255) NOT NULL,
    idPermission INT          NOT NULL,
    assignDate   VARCHAR(255),
    status       VARCHAR(255) NOT NULL DEFAULT 'active',
    notes        VARCHAR(255),
    FOREIGN KEY (idRole)       REFERENCES tblRole(idRole)             ON DELETE CASCADE,
    FOREIGN KEY (idPermission) REFERENCES Permission(idPermission)    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- 5. tblAccount  (liên kết Staff 1-1, Role N-1)
-- ============================================================
CREATE TABLE IF NOT EXISTS tblAccount (
    idAcc      INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    idRole     VARCHAR(255) NOT NULL,
    status     VARCHAR(255) NOT NULL DEFAULT 'active',
    staffId    INT          NULL,
    createDate DATETIME     DEFAULT CURRENT_TIMESTAMP,
    updateDate DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (idRole)   REFERENCES tblRole(idRole)   ON DELETE RESTRICT,
    FOREIGN KEY (staffId)  REFERENCES tblStaff(ID)      ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- Dữ liệu mẫu – tblStaff
-- ============================================================
INSERT INTO tblStaff (ID, fullname, role, tel, email) VALUES
(1, 'Nguyen Van An',   'Admin',    '0901234567', 'an.nguyen@lib.vn'),
(2, 'Tran Thi Binh',   'Manager',  '0912345678', 'binh.tran@lib.vn'),
(3, 'Le Van Cuong',    'Employee', '0923456789', 'cuong.le@lib.vn'),
(4, 'Pham Thi Dung',   'Employee', '0934567890', 'dung.pham@lib.vn'),
(5, 'Hoang Van Em',    'Employee', '0945678901', 'em.hoang@lib.vn');

-- ============================================================
-- Dữ liệu mẫu – tblRole
-- ============================================================
INSERT INTO tblRole (idRole, roleName, description) VALUES
('ROLE_ADMIN',    'Admin',    'Quản trị hệ thống'),
('ROLE_MANAGER',  'Manager',  'Quản lý nhân viên'),
('ROLE_EMPLOYEE', 'Employee', 'Nhân viên thông thường');

-- ============================================================
-- Dữ liệu mẫu – Permission
-- ============================================================
INSERT INTO Permission (permissionName, description) VALUES
('MANAGE_STAFF',   'Thêm, sửa, xóa, tìm kiếm nhân viên'),
('VIEW_STAFF',     'Xem danh sách nhân viên'),
('MANAGE_ACCOUNT', 'Thêm, sửa, xóa tài khoản'),
('VIEW_ACCOUNT',   'Xem danh sách tài khoản');

-- ============================================================
-- Dữ liệu mẫu – tblRole_Permission
-- ============================================================
-- Admin: có tất cả quyền
INSERT INTO tblRole_Permission (id, idRole, idPermission, assignDate, status) VALUES
('RP001', 'ROLE_ADMIN', 1, NOW(), 'active'),
('RP002', 'ROLE_ADMIN', 2, NOW(), 'active'),
('RP003', 'ROLE_ADMIN', 3, NOW(), 'active'),
('RP004', 'ROLE_ADMIN', 4, NOW(), 'active');

-- Manager: quản lý và xem nhân viên
INSERT INTO tblRole_Permission (id, idRole, idPermission, assignDate, status) VALUES
('RP005', 'ROLE_MANAGER', 1, NOW(), 'active'),
('RP006', 'ROLE_MANAGER', 2, NOW(), 'active');

-- Employee: chỉ xem nhân viên
INSERT INTO tblRole_Permission (id, idRole, idPermission, assignDate, status) VALUES
('RP007', 'ROLE_EMPLOYEE', 2, NOW(), 'active');

-- ============================================================
-- Dữ liệu mẫu – tblAccount
-- ============================================================
INSERT INTO tblAccount (username, password, idRole, status, staffId) VALUES
('admin',   '123456', 'ROLE_ADMIN',    'active', 1),
('manager', '123456', 'ROLE_MANAGER',  'active', 2),
('cuong',   '123456', 'ROLE_EMPLOYEE', 'active', 3),
('dung',    '123456', 'ROLE_EMPLOYEE', 'active', 4),
('em',      '123456', 'ROLE_EMPLOYEE', 'active', 5);
