package com.brlopes.dto;

import com.brlopes.Model.enums.LoginRoles;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * The registerDTO class is a record class in Java.
 * It is used to store and retrieve the login, password, and role details for a new user registration.
 * This class is typically used to transfer data between processes.
 * As a record, it has a concise syntax for declaring simple, immutable data carrier classes and
 * it automatically provides several methods such as equals, hashCode, toString, getters etc.
 *
 * @param login This is the login provided by the user for registration. It must be at least 4 characters long and can only contain letters and numbers.
 * @param password This is the password provided by the user for registration.
 * @param role This is the role assigned to the user at registration.
 */
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