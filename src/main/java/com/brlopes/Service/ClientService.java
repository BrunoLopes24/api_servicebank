package com.brlopes.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.brlopes.Model.Client;
import com.brlopes.Repository.ClientRepo;
import com.brlopes.Service.exceptions.DatabaseException;
import com.brlopes.Service.exceptions.NoClientIdException;
import com.brlopes.Service.exceptions.ResourceNotFoundException;
import com.brlopes.Service.exceptions.TokenExpiredException;

@Service
public class ClientService {
    @Autowired
    private ClientRepo clientRepo;
    
    public Client insert(Client client){ // Create
        return clientRepo.save(client);
    }
    
    public Iterable<Client> findAll(String token){ // Read
        try {
            verifyToken(token);
            return clientRepo.findAll();
        }catch (com.auth0.jwt.exceptions.TokenExpiredException e) {
            throw new TokenExpiredException("");
        }
    }
    
    public Client findById(String token, Long id){ // ReadbyId
        try {
            verifyToken(token);
            Optional<Client> client = clientRepo.findById(id);
            return client.orElseThrow(()-> new ResourceNotFoundException(id));
        } catch (com.auth0.jwt.exceptions.TokenExpiredException e) {
            throw new TokenExpiredException("");
        } catch (ResourceNotFoundException e){
            throw new NoClientIdException("");
        }
    }
    
    public Client updateById(String token, Long id, Client newClient) { // Update
        try {
            verifyToken(token);
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
        }catch (com.auth0.jwt.exceptions.TokenExpiredException e) {
            throw new TokenExpiredException("");
        }
    }
    
    
    public void deletebyId(String token, Long id){ // Delete
        try {
            verifyToken(token);
            clientRepo.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        }catch (DataIntegrityViolationException e){
            throw new DatabaseException(e.getMessage());
        } catch (com.auth0.jwt.exceptions.TokenExpiredException e) {
            throw new TokenExpiredException("");
        }
    }
    
    
    private void verifyToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256("servicebank");
        JWT.require(algorithm).build().verify(token);
    }
}
