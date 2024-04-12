package com.brlopes.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.brlopes.Model.Client;
import com.brlopes.Repository.ClientRepo;
import com.brlopes.Service.exceptions.DatabaseException;
import com.brlopes.Service.exceptions.ResourceNotFoundException;

@Service
public class ClientService {
    @Autowired
    private ClientRepo clientRepo;
    
    public Client insert(Client client){ // Create
        return clientRepo.save(client);
    }
    
    public Iterable<Client> findAll(){ // Read
        return clientRepo.findAll();
    }
    
    public Client findById(Long id){ // ReadbyId
        Optional<Client> client = clientRepo.findById(id);
        return client.orElseThrow(()-> new ResourceNotFoundException(id));
    }
    
    public Client updateById(Long id, Client newClient) { // Update
        Optional<Client> optionalClient = clientRepo.findById(id);
        if (optionalClient.isPresent()) {
            Client existingClient = optionalClient.get();
            
            existingClient.setName(newClient.getName());
            
            return clientRepo.save(existingClient); // Devolve o cliente atualizado
        } else {
            throw new ResourceNotFoundException(id);
        }
    }
    
    public void deletebyId(Long id){ // Delete
        try {
            clientRepo.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        }catch (DataIntegrityViolationException e){
            throw new DatabaseException(e.getMessage());
        }
    }
    
    
}
