package com.brlopes.dto;

import com.brlopes.Model.enums.LoginRoles;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record registerDTO (@NotBlank String login,@NotBlank String password,@NotNull LoginRoles role){
}
