package com.brlopes.Service;

import com.brlopes.Model.Client;
import com.brlopes.Model.Transactions;
import com.brlopes.Repository.ClientRepo;
import com.brlopes.Repository.TransactionRepo;
import com.brlopes.Service.exceptions.DataIntegrityViolationException;
import com.brlopes.Service.exceptions.DatabaseException;
import com.brlopes.Service.exceptions.ResourceNotFoundException;
import com.brlopes.Service.exceptions.SameClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * The TransactionsService class provides methods for managing transactions.
 * It uses the @Service annotation to indicate that it is a Spring Boot service.
 * It uses the TransactionRepo and ClientRepo repositories for database operations.
 */
@Service
public class TransactionsService {

    /**
     * The TransactionRepo repository used for transaction database operations.
     */
    @Autowired
    private TransactionRepo transactionRepo;

    /**
     * The ClientRepo repository used for client database operations.
     */
    @Autowired
    private ClientRepo clientRepo;

    /**
     * This method inserts a new transaction into the database.
     * It performs several checks and calculations before inserting the transaction.
     *
     * @param transaction the transaction to be inserted.
     * @param clientName the name of the client making the transaction.
     * @param destinyClientName the name of the client receiving the transaction.
     * @return the inserted transaction.
     * @throws DataIntegrityViolationException if one of the clients does not exist or if the clients are the same.
     * @throws IllegalArgumentException if the transaction value or tax is null, or if the sender does not have enough balance.
     * @throws SameClientException if the clients are the same.
     */
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
            if (client.getClient_id() != null && client.getClient_id().equals(destinyClient.getClient_id())) {
                throw new SameClientException("The clients cannot be the same");
            }

            // Checks if the sender has enough balance
            Double value = transaction.getValue();
            if (value == null) {
                throw new IllegalArgumentException("Transaction value cannot be null");
            }
            if (client.getBalance() < value) {
                throw new IllegalArgumentException("Sender does not have enough balance for the transaction");
            }

            // Calculate the total amount of the transaction
            Double tax = transaction.getTax();
            if (tax == null) {
                throw new IllegalArgumentException("Transaction tax cannot be null");
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


    /**
     * This method retrieves all transactions from the database.
     *
     * @return an Iterable of all transactions.
     */
    public Iterable<Transactions> findAll(){ // Read
        return transactionRepo.findAll();
    }

    /**
     * This method retrieves a transaction by ID from the database.
     * It throws a ResourceNotFoundException if the transaction does not exist.
     *
     * @param id the ID of the transaction.
     * @return the transaction with the given ID.
     * @throws ResourceNotFoundException if the transaction does not exist.
     */
    public Transactions findById(Long id){
        Optional<Transactions> transaction = transactionRepo.findById(id);
        return transaction.orElseThrow(()-> new ResourceNotFoundException(id));
    }

    /**
     * This method deletes a transaction by ID from the database.
     * It throws a ResourceNotFoundException if the transaction does not exist.
     *
     * @param id the ID of the transaction.
     * @throws ResourceNotFoundException if the transaction does not exist.
     * @throws DatabaseException if a database error occurs.
     */
    public void deletebyId(Long id){ // Delete
        try {
            transactionRepo.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        }catch (DataIntegrityViolationException e){
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * This method adds a deposit to a client's account.
     * It updates the client's balance with the transaction amount.
     *
     * @param transaction the transaction to be added.
     * @param clientName the name of the client.
     * @return the added transaction.
     * @throws DataIntegrityViolationException if the client does not exist.
     */
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