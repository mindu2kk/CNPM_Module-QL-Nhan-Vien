package model;

import java.io.Serializable;

public class Permission implements Serializable {

    private int    idPermission;
    private String permissionName;
    private String description;

    public Permission() {}

    public Permission(int idPermission, String permissionName, String description) {
        this.idPermission   = idPermission;
        this.permissionName = permissionName;
        this.description    = description;
    }

    public int    getIdPermission()                      { return idPermission; }
    public void   setIdPermission(int id)                { this.idPermission = id; }

    public String getPermissionName()                    { return permissionName; }
    public void   setPermissionName(String name)         { this.permissionName = name; }

    public String getDescription()                       { return description; }
    public void   setDescription(String d)               { this.description = d; }

    @Override
    public String toString() { return permissionName; }
}
