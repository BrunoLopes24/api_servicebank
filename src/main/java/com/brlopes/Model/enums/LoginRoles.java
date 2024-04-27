package com.brlopes.Model.enums;

/**
 * The LoginRoles enum is used to define the roles for the users in the system.
 * It has two roles: ADMIN and CLIENT.
 * Each role is represented as a String.
 * This enum provides a method to get the role as a String.
 */
public enum LoginRoles {
    /**
     * The ADMIN role. It is represented as a String "admin".
     */
    ADMIN("admin"),

    /**
     * The CLIENT role. It is represented as a String "client".
     */
    CLIENT("client");

    /**
     * The role as a String.
     */
    private String role;

    /**
     * Constructor for the enum.
     * It is used to set the role.
     *
     * @param role the role as a String.
     */
    private LoginRoles(String role){
        this.role = role;
    }

    /**
     * This method returns the role as a String.
     *
     * @return the role as a String.
     */
    public String getRole() {
        return role;
    }
}