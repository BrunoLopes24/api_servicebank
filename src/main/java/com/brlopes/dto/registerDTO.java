package com.brlopes.dto;

import com.brlopes.Model.enums.LoginRoles;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record registerDTO(
		@NotBlank
		@Size(min = 4, message = "Login must be at least 4 characters long")
		@Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Login can only contain letters and numbers")
		String login,

		@NotBlank
		String password,

		@NotNull
		LoginRoles role
) {}
