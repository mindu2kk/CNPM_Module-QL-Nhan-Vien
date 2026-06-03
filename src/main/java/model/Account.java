package model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Account implements Serializable {

    private int     idAcc;
    private String  username;
    private String  password;
    private String  idRole;       // FK → tblRole.idRole
    private String  roleName;     // tên hiển thị (Manager, Admin, Employee...)
    private String  status;
    private Integer staffId;
    private Date    createDate;
    private Date    updateDate;

    // Tập quyền đã gán cho role của account này
    private Set<String> permissions = new HashSet<>();

    public Account() {}

    // ---------- getters / setters ----------
    public int     getIdAcc()                    { return idAcc; }
    public void    setIdAcc(int idAcc)           { this.idAcc = idAcc; }

    public String  getUsername()                 { return username; }
    public void    setUsername(String v)         { this.username = v; }

    public String  getPassword()                 { return password; }
    public void    setPassword(String v)         { this.password = v; }

    public String  getIdRole()                   { return idRole; }
    public void    setIdRole(String v)           { this.idRole = v; }

    public String  getRoleName()                 { return roleName; }
    public void    setRoleName(String v)         { this.roleName = v; }

    // getRole() giữ tương thích với code View cũ — trả về roleName
    public String  getRole()                     { return roleName; }

    public String  getStatus()                   { return status; }
    public void    setStatus(String v)           { this.status = v; }

    public Integer getStaffId()                  { return staffId; }
    public void    setStaffId(Integer v)         { this.staffId = v; }

    public Date    getCreateDate()               { return createDate; }
    public void    setCreateDate(Date d)         { this.createDate = d; }

    public Date    getUpdateDate()               { return updateDate; }
    public void    setUpdateDate(Date d)         { this.updateDate = d; }

    public Set<String> getPermissions()          { return permissions; }
    public void setPermissions(Set<String> p)    { this.permissions = p; }

    /** Kiểm tra xem account có quyền cụ thể không */
    public boolean hasPermission(String permissionName) {
        return permissions.contains(permissionName);
    }
}
