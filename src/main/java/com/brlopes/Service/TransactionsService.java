package com.brlopes.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.brlopes.Model.Client;
import com.brlopes.Model.Transactions;
import com.brlopes.Repository.ClientRepo;
import com.brlopes.Repository.TransactionRepo;
import com.brlopes.Service.exceptions.DataIntegrityViolationException;
import com.brlopes.Service.exceptions.ResourceNotFoundException;
import com.brlopes.Service.exceptions.SameClientException;
import com.brlopes.Service.exceptions.TokenExpiredException;

@Service
public class TransactionsService {
    
    @Autowired
    private TransactionRepo transactionRepo;
    @Autowired
    private ClientRepo clientRepo;
    
    
    public Transactions insert(String token, Transactions transaction) {
        try {
            verifyToken(token);
            
            if (transaction.getClient() == null || transaction.getDestinyClient() == null) {
                throw new IllegalArgumentException("O cliente ou o cliente destino não podem ser nulos");
            }
            
            // Verificando se os clientes existem na base de dados
            Client client = transaction.getClient();
            Client destinyClient = transaction.getDestinyClient();
            Long clientId = client.getClient_id();
            Long destinyClientId = destinyClient.getClient_id();
            
            // Verificando se os IDs dos clientes são iguais
            if (clientId.equals(destinyClientId)) {
                throw new SameClientException("");
            }
            
            if (!clientRepo.existsById(clientId) || !clientRepo.existsById(destinyClientId)) {
                throw new DataIntegrityViolationException("Um dos clientes não existe na base de dados");
            }
            
            // Salvando a transação na base de dados
            return transactionRepo.save(transaction);
        } catch (com.auth0.jwt.exceptions.TokenExpiredException e) {
            throw new TokenExpiredException("");
        } catch (DataIntegrityViolationException | IllegalArgumentException e) {
            throw e;
        }
    }
    
    private void verifyToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256("servicebank");
        JWT.require(algorithm).build().verify(token);
    }
    
    public Iterable<Transactions> findAll(String token){ // Read
        try {
            verifyToken(token);
            return transactionRepo.findAll();
        } catch (com.auth0.jwt.exceptions.TokenExpiredException e) {
            throw new TokenExpiredException("");
        }
    }
    
    public Transactions findById(String token, Long id){
        try{
            verifyToken(token);
            Optional<Transactions> transaction = transactionRepo.findById(id);
            return transaction.orElseThrow(()-> new ResourceNotFoundException(id));
            
        } catch (com.auth0.jwt.exceptions.TokenExpiredException e) {
            throw new TokenExpiredException("");
        }
    }
}