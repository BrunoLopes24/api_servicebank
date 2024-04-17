package com.brlopes.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.brlopes.Model.Client;
import com.brlopes.Model.Transactions;
import com.brlopes.Repository.ClientRepo;
import com.brlopes.Repository.TransactionRepo;
import com.brlopes.Service.exceptions.DataIntegrityViolationException;
import com.brlopes.Service.exceptions.DatabaseException;
import com.brlopes.Service.exceptions.ResourceNotFoundException;
import com.brlopes.Service.exceptions.SameClientException;

@Service
public class TransactionsService {
    
    @Autowired
    private TransactionRepo transactionRepo;
    @Autowired
    private ClientRepo clientRepo;
    
    
    public Transactions insert(Transactions transaction) {
        try {
            
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
        } catch (DataIntegrityViolationException | IllegalArgumentException e) {
            throw e;
        }
    }
    
    public Iterable<Transactions> findAll(){ // Read
        return transactionRepo.findAll();
    }
    
    public Transactions findById(Long id){
        Optional<Transactions> transaction = transactionRepo.findById(id);
        return transaction.orElseThrow(()-> new ResourceNotFoundException(id));
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