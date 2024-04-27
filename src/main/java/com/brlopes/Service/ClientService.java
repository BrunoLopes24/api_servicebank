package com.brlopes.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.brlopes.Model.Client;
import com.brlopes.Repository.ClientRepo;
import com.brlopes.Service.exceptions.DatabaseException;
import com.brlopes.Service.exceptions.NoClientIdException;
import com.brlopes.Service.exceptions.ResourceNotFoundException;

/**
 * The ClientService class provides CRUD operations for the Client entity.
 * It uses the @Service annotation to indicate that it is a Spring Boot service.
 * It uses the ClientRepo repository for database operations.
 */
@Service
public class ClientService {

    /**
     * The ClientRepo repository used for database operations.
     */
    @Autowired
    private ClientRepo clientRepo;

    /**
     * This method inserts a new client into the database.
     *
     * @param client the client to be inserted.
     * @return the inserted client.
     */
    public Client insert(Client client){ // Create
        return clientRepo.save(client);
    }

    /**
     * This method retrieves all clients from the database.
     *
     * @return an Iterable of all clients.
     */
    public Iterable<Client> findAll(){ // Read
        return clientRepo.findAll();
    }

    /**
     * This method retrieves a client by ID from the database.
     * It throws a ResourceNotFoundException if the client does not exist.
     *
     * @param id the ID of the client.
     * @return the client with the given ID.
     * @throws NoClientIdException if the client does not exist.
     */
    public Client findById(Long id){ // ReadbyId
        try {
            Optional<Client> client = clientRepo.findById(id);
            return client.orElseThrow(()-> new ResourceNotFoundException(id));
        } catch (ResourceNotFoundException e){
            throw new NoClientIdException("");
        }
    }

    /**
     * This method updates a client by ID in the database.
     * It throws a ResourceNotFoundException if the client does not exist.
     *
     * @param id the ID of the client.
     * @param newClient the client with the new data.
     * @return the updated client.
     * @throws NoClientIdException if the client does not exist.
     */
    public Client updateById(Long id, Client newClient) { // Update
        try {
            Optional<Client> optionalClient = clientRepo.findById(id);
            if (optionalClient.isPresent()) {
                Client existingClient = optionalClient.get();

                existingClient.setName(newClient.getName());

                return clientRepo.save(existingClient); // Devolve o cliente atualizado
            } else {
                throw new ResourceNotFoundException(id);
            }
        } catch (ResourceNotFoundException e){
            throw new NoClientIdException("");
        }
    }

    /**
     * This method deletes a client by ID from the database.
     * It throws a ResourceNotFoundException if the client does not exist.
     *
     * @param id the ID of the client.
     * @throws ResourceNotFoundException if the client does not exist.
     * @throws DatabaseException if a database error occurs.
     */
    public void deletebyId(Long id){ // Delete
        try {
            clientRepo.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        }catch (DataIntegrityViolationException e){
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * This method checks the balance of a client by ID.
     * It throws a NoClientIdException if the client does not exist.
     *
     * @param id the ID of the client.
     * @return the balance of the client.
     * @throws NoClientIdException if the client does not exist.
     */
    public double checkBalancebyId(Long id) {
        Optional<Client> optionalClient = clientRepo.findById(id);
        if (optionalClient.isPresent()) {
            Client client = optionalClient.get();
            return client.getBalance();
        } else {
            throw new NoClientIdException("Client not found with id: " + id);
        }
    }
}