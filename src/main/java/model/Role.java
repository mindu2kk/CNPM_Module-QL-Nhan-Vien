package model;

import java.io.Serializable;

public class Role implements Serializable {

    private String idRole;
    private String roleName;
    private String description;

    public Role() {}

    public Role(String idRole, String roleName, String description) {
        this.idRole      = idRole;
        this.roleName    = roleName;
        this.description = description;
    }

    public String getIdRole()                    { return idRole; }
    public void   setIdRole(String idRole)       { this.idRole = idRole; }

    public String getRoleName()                  { return roleName; }
    public void   setRoleName(String roleName)   { this.roleName = roleName; }

    public String getDescription()               { return description; }
    public void   setDescription(String d)       { this.description = d; }

    @Override
    public String toString() { return roleName; }
}
