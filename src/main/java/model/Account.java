package model;

import java.io.Serializable;
import java.util.Date;

public class Account implements Serializable {

    // Các hằng phân quyền
    public static final String ROLE_ADMIN    = "Admin";
    public static final String ROLE_MANAGER  = "Manager";
    public static final String ROLE_EMPLOYEE = "Employee";

    private String idAcc;
    private String username;
    private String password;
    private String role;       // Admin | Manager | Employee
    private String status;     // active | inactive
    private Integer staffId;   // liên kết với tblStaff
    private Date createDate;
    private Date updateDate;

    public Account() { super(); }

    public Account(String idAcc, String username, String password,
                   String role, String status, Integer staffId,
                   Date createDate, Date updateDate) {
        this.idAcc      = idAcc;
        this.username   = username;
        this.password   = password;
        this.role       = role;
        this.status     = status;
        this.staffId    = staffId;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    // ---------- getters / setters ----------
    public String  getIdAcc()                    { return idAcc; }
    public void    setIdAcc(String idAcc)        { this.idAcc = idAcc; }

    public String  getUsername()                 { return username; }
    public void    setUsername(String username)  { this.username = username; }

    public String  getPassword()                 { return password; }
    public void    setPassword(String password)  { this.password = password; }

    public String  getRole()                     { return role; }
    public void    setRole(String role)          { this.role = role; }

    public String  getStatus()                   { return status; }
    public void    setStatus(String status)      { this.status = status; }

    public Integer getStaffId()                  { return staffId; }
    public void    setStaffId(Integer staffId)   { this.staffId = staffId; }

    public Date    getCreateDate()               { return createDate; }
    public void    setCreateDate(Date d)         { this.createDate = d; }

    public Date    getUpdateDate()               { return updateDate; }
    public void    setUpdateDate(Date d)         { this.updateDate = d; }

    /** Tiện ích kiểm tra quyền */
    public boolean isAdmin()    { return ROLE_ADMIN.equalsIgnoreCase(role); }
    public boolean isManager()  { return ROLE_MANAGER.equalsIgnoreCase(role); }
    public boolean isEmployee() { return ROLE_EMPLOYEE.equalsIgnoreCase(role); }
}
