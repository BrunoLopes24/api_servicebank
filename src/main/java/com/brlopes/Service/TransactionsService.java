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
    
    
    public Transactions insert(Transactions transaction, String clientName, String destinyClientName) {
        try {
            // Verifies that customer names have been provided
            if (clientName == null || destinyClientName == null) {
                throw new IllegalArgumentException("The name of the destiny client or client cannot be null");
            }
            
            // Search for customers by name
            Client client = clientRepo.findByName(clientName);
            Client destinyClient = clientRepo.findByName(destinyClientName);
            
            // Checks if customers exist
            if (client == null || destinyClient == null) {
                throw new DataIntegrityViolationException("One of the clients does not exist in the database");
            }
            
            // Verify that customers are the same
            if (client.getClient_id().equals(destinyClient.getClient_id())) {
                throw new SameClientException("The clients cannot be the same");
            }
            
            // Checks if the sender has enough balance
            if (client.getBalance() < transaction.getValue()) {
                throw new IllegalArgumentException("Sender does not have enough balance for the transaction");
            }
            
            // Associates customers with the transaction
            transaction.setClient(client);
            transaction.setDestinyClient(destinyClient);
            
            // Calculates the total amount of the transaction
            double totalAmount = transaction.getValue() + transaction.getTax();
            transaction.setTotalAmmount(totalAmount);
            
            // Updates the balance of the sender and receiver
            client.setBalance(client.getBalance() - transaction.getValue());
            destinyClient.setBalance(destinyClient.getBalance() + transaction.getValue());
            
            // Updates the balance of the sender and receiver
            clientRepo.save(client);
            clientRepo.save(destinyClient);
            
            // Saves the transaction to the database
            return transactionRepo.save(transaction);
        } catch (DataIntegrityViolationException | IllegalArgumentException | SameClientException e) {
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
    
    public Transactions addDeposit(Transactions transaction, String clientName) {
        // Verify that the client exists in the database
        Client client = clientRepo.findByName(clientName);
        if (client == null) {
            throw new DataIntegrityViolationException("Client does not exist in the database.");
        }
        
        // Associates the customer with the transaction
        transaction.setClient(client);
        
        // Saves the transaction to the database
        Transactions savedTransaction = transactionRepo.save(transaction);
        
        // Updates the client's balance with the transaction amount
        double updatedBalance = client.getBalance() + transaction.getValue();
        client.setBalance(updatedBalance);
        
        // Saves the client update to the database
        clientRepo.save(client);
        
        return savedTransaction;
    }
    
}