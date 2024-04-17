package com.brlopes.dto;

import com.brlopes.Model.enums.LoginRoles;

public record registerDTO (String login, String password, LoginRoles role){
    
}
