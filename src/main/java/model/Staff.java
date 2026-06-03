package model;

import java.io.Serializable;
import java.util.Date;

public class Staff implements Serializable {

    private int    id;          // ID : int
    private String fullname;    // fullname : String
    private String role;        // role : String  ("Manager" | "Employee" | "Admin")
    private String tel;         // tel : String
    private String email;       // email : String
    private Date   createDate;  // createDate : Date  (theo diagram EditStaffFrm)
    private String status;      // status : String    (theo diagram EditStaffFrm)

    public Staff() { super(); }

    public Staff(int id, String fullname, String role,
                 String tel, String email, Date createDate, String status) {
        this.id         = id;
        this.fullname   = fullname;
        this.role       = role;
        this.tel        = tel;
        this.email      = email;
        this.createDate = createDate;
        this.status     = status;
    }

    // Constructor rút gọn (tương thích ngược)
    public Staff(int id, String fullname, String role, String tel, String email) {
        this(id, fullname, role, tel, email, null, "active");
    }

    public int     getId()                      { return id; }
    public void    setId(int id)                { this.id = id; }

    public String  getFullname()                { return fullname; }
    public void    setFullname(String f)        { this.fullname = f; }

    public String  getRole()                    { return role; }
    public void    setRole(String role)         { this.role = role; }

    public String  getTel()                     { return tel; }
    public void    setTel(String tel)           { this.tel = tel; }

    public String  getEmail()                   { return email; }
    public void    setEmail(String email)       { this.email = email; }

    public Date    getCreateDate()              { return createDate; }
    public void    setCreateDate(Date d)        { this.createDate = d; }

    public String  getStatus()                  { return status; }
    public void    setStatus(String status)     { this.status = status; }
}
