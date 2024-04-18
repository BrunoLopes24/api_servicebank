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
            
            if (clientName == null || destinyClientName == null) {
                throw new IllegalArgumentException("O nome do cliente ou do cliente destino não pode ser nulo");
            }
            
            // Procura os clientes pela nome
            Client client = clientRepo.findByName(clientName);
            Client destinyClient = clientRepo.findByName(destinyClientName);
            
            if (client == null || destinyClient == null) {
                throw new DataIntegrityViolationException("Um dos clientes não existe na base de dados");
            }
            
            // Verifica se os clientes são os mesmos
            if (client.getClient_id().equals(destinyClient.getClient_id())) {
                throw new SameClientException("Os clientes envolvidos na transação não podem ser os mesmos");
            }
            
            // Associa clientes à transação
            transaction.setClient(client);
            transaction.setDestinyClient(destinyClient);
            
            // Guarda a transação na base de dados
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
}