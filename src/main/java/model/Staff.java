package model;

import java.io.Serializable;

public class Staff implements Serializable {

    private int    ID;
    private String fullname;
    private String role;
    private String tel;
    private String email;

    public Staff() { super(); }

    public Staff(int ID, String fullname, String role, String tel, String email) {
        this.ID       = ID;
        this.fullname = fullname;
        this.role     = role;
        this.tel      = tel;
        this.email    = email;
    }

    public int    getId()                   { return ID; }
    public void   setId(int ID)             { this.ID = ID; }

    public String getFullname()             { return fullname; }
    public void   setFullname(String f)     { this.fullname = f; }

    public String getRole()                 { return role; }
    public void   setRole(String role)      { this.role = role; }

    public String getTel()                  { return tel; }
    public void   setTel(String tel)        { this.tel = tel; }

    public String getEmail()                { return email; }
    public void   setEmail(String email)    { this.email = email; }
}
