package com.brlopes.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.brlopes.Model.Login;

/**
 * The LoginRepo interface extends JpaRepository.
 * It provides CRUD operations for the Login entity.
 * It also provides a method to find a login by username.
 */
public interface LoginRepo extends JpaRepository<Login, Long> {

    /**
     * This method finds a login by username.
     * It returns a UserDetails object if a login with the given username exists.
     * Otherwise, it returns null.
     *
     * @param username the username of the login.
     * @return a UserDetails object or null.
     */
    UserDetails findByUsername(String username);
}