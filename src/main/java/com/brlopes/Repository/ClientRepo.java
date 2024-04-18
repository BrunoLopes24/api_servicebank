package com.brlopes.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brlopes.Model.Client;

public interface ClientRepo extends JpaRepository<Client, Long> {
    Client findByUsername(String username);
}
