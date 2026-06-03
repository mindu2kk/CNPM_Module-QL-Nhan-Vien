package model;

import java.io.Serializable;
import java.util.Date;

public class Account implements Serializable {

    private String idAcc;
    private String username;
    private String password;
    private String status;
    private Date createDate;
    private Date updateDate;

    public Account() { super(); }

    public Account(String idAcc, String username, String password,
                   String status, Date createDate, Date updateDate) {
        this.idAcc      = idAcc;
        this.username   = username;
        this.password   = password;
        this.status     = status;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public String getIdAcc()                    { return idAcc; }
    public void   setIdAcc(String idAcc)        { this.idAcc = idAcc; }

    public String getUsername()                 { return username; }
    public void   setUsername(String username)  { this.username = username; }

    public String getPassword()                 { return password; }
    public void   setPassword(String password)  { this.password = password; }

    public String getStatus()                   { return status; }
    public void   setStatus(String status)      { this.status = status; }

    public Date getCreateDate()                         { return createDate; }
    public void setCreateDate(Date createDate)          { this.createDate = createDate; }

    public Date getUpdateDate()                         { return updateDate; }
    public void setUpdateDate(Date updateDate)          { this.updateDate = updateDate; }
}
