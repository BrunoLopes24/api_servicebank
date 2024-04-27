package com.brlopes.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.brlopes.Model.Client;

/**
 * The ClientRepo interface extends JpaRepository.
 * It provides CRUD operations for the Client entity.
 * It also provides methods to find a client by username and name.
 */
public interface ClientRepo extends JpaRepository<Client, Long> {

    /**
     * This method finds a client by username.
     * It returns a Client object if a client with the given username exists.
     * Otherwise, it returns null.
     *
     * @param username the username of the client.
     * @return a Client object or null.
     */
    Client findByUsername(String username);

    /**
     * This method finds a client by name.
     * It returns a Client object if a client with the given name exists.
     * Otherwise, it returns null.
     *
     * @param name the name of the client.
     * @return a Client object or null.
     */
    Client findByName(String name);
}