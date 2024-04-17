package com.brlopes.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.brlopes.Model.Login;

public interface LoginRepo extends JpaRepository<Login, Long> {
    UserDetails findByUsername(String username);
}