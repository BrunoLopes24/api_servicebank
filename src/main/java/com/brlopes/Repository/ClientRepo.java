package com.brlopes.Repository;

import org.springframework.data.repository.CrudRepository;

import com.brlopes.Model.Client;

public interface ClientRepo extends CrudRepository<Client, Long> {
    Client findByUsername(String username);
}
