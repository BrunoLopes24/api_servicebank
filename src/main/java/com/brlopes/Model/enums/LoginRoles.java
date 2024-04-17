package com.brlopes.Model.enums;

public enum LoginRoles {
    ADMIN("admin"),
    CLIENT("client");
    
    private String role;
    
    private LoginRoles(String role){
        this.role = role;
    }
    
    public String getRole() {
        return role;
    }
}
