package com.brlopes.Controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.brlopes.Model.Transactions;
import com.brlopes.Service.TransactionsService;
import com.brlopes.Service.exceptions.DataIntegrityViolationException;
import com.brlopes.Service.exceptions.ErrorResponse;
import com.brlopes.Service.exceptions.SameClientException;
import com.brlopes.dto.TransactionDTO;

/**
 * The TransactionController class handles transaction-related requests.
 * It provides endpoints for creating, reading, and deleting transactions, as well as depositing money to a client account.
 */
@RestController
@RequestMapping("/transactions")
public class TransactionController {
    
    @Autowired
    private TransactionsService transactionsService;

    /**
     * This method handles requests to get all transactions.
     * It retrieves all transactions from the database and returns them as a list of TransactionDTOs.
     *
     * @return ResponseEntity<?> This returns a list of all transactions if successful.
     */
    @GetMapping("/") // Read - NORMAL CLIENT ROLE
    public ResponseEntity<?> findAll() {
        Iterable<Transactions> transactions = transactionsService.findAll();
        
        List<TransactionDTO> transactionList = StreamSupport.stream(transactions.spliterator(), false)
        .map(this::mapToDTO)
        .toList();
        
        return ResponseEntity.ok().body(transactionList);
    }

    /**
     * This method maps a Transactions object to a TransactionDTO.
     * It handles null cases for client, destiny client, and date.
     *
     * @param transaction This is the transaction to be mapped.
     * @return TransactionDTO This returns the mapped TransactionDTO.
     */
    private TransactionDTO mapToDTO(@NotNull Transactions transaction) {
        // Handle null cases for client, destiny client, and date
        String clientName = transaction.getClient() != null ? transaction.getClient().getName() : null;
        String destinyClientName = transaction.getDestinyClient() != null ? transaction.getDestinyClient().getName() : null;
        String transactionDate = transaction.getDate() != null ? transaction.getDate().toString() : null;
        
        // Return a new TransactionDTO with the mapped properties
        return new TransactionDTO(
        transaction.getTotalAmmount(),
        clientName,
        destinyClientName,
        transactionDate
        );
    }


    /**
     * This method handles requests to add a new transaction.
     * It creates a new transaction with the provided data and saves it to the database.
     *
     * @param request This is the transaction data provided in the request body.
     * @return ResponseEntity<?> This returns an OK status code if the transaction is successfully added, otherwise an ErrorResponse.
     */
    @PostMapping("/add")
    public ResponseEntity<?> addTransaction(@NotNull @RequestBody TransactionDTO request) {
        Optional<String> clientName = Optional.ofNullable(request.getClientName());
        Optional<String> destinyClientName = Optional.ofNullable(request.getDestinyClientName());
        
        if (!clientName.isPresent() || !destinyClientName.isPresent()) {
            return buildErrorResponse(HttpStatus.BAD_REQUEST, "The client or destiny client cannot be null");
        }
        
        try {
            transactionsService.insert(request.getTransaction(), clientName.get(), destinyClientName.get());
            return ResponseEntity.ok().build();
        } catch (DataIntegrityViolationException e) {
            return buildErrorResponse(HttpStatus.BAD_REQUEST, "One of the customers does not exist in the database");
        } catch (IllegalArgumentException e){
            return buildErrorResponse(HttpStatus.BAD_REQUEST, "The client or destiny client cannot be null");
        } catch (SameClientException e){
            return buildErrorResponse(HttpStatus.BAD_REQUEST, "Client and destiny client name cannot be the same");
        }
    }

    /**
     * This method builds an ErrorResponse with the provided status and message.
     *
     * @param status This is the status to be set in the ErrorResponse.
     * @param message This is the message to be set in the ErrorResponse.
     * @return ResponseEntity<ErrorResponse> This returns the built ErrorResponse.
     */
    @NotNull
    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String message) {
        ErrorResponse errorResponse = new ErrorResponse(status, message);
        return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
    }

    /**
     * This method handles requests to get a transaction by their ID.
     * It retrieves the transaction with the provided ID from the database and returns it.
     *
     * @param id This is the ID of the transaction to be retrieved.
     * @return ResponseEntity<?> This returns the transaction if found.
     */
    @GetMapping("/{id}") // Read (by ID) - ADMIN ROLE
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Transactions transaction = transactionsService.findById(id);
        return ResponseEntity.ok().body(transaction);
    }

    /**
     * This method handles requests to delete a transaction by their ID.
     * It deletes the transaction with the provided ID from the database.
     *
     * @param id This is the ID of the transaction to be deleted.
     * @return ResponseEntity<ErrorResponse> This returns a no content status code if the transaction is successfully deleted.
     */
    @DeleteMapping("/{id}") // Delete (by ID) - ADMIN ROLE
    public ResponseEntity<ErrorResponse> deletebyId(@PathVariable Long id) {
        transactionsService.deletebyId(id);
        return ResponseEntity.noContent().build();
        
    }

    /**
     * This method handles requests to deposit money to a client account.
     * It creates a new deposit transaction with the provided data and saves it to the database.
     *
     * @param transaction This is the transaction data provided in the request body.
     * @param clientName This is the name of the client to deposit money to.
     * @return ResponseEntity<Transactions> This returns the created deposit transaction if successful, otherwise an ErrorResponse.
     */
    @PostMapping("/deposit") // Deposit money to Client account - ADMIN ROLE
    public ResponseEntity<Transactions> addDeposit(@RequestBody Transactions transaction, @RequestParam String clientName) {
        try {
            Transactions addedTransaction = transactionsService.addDeposit(transaction, clientName);
            return ResponseEntity.ok(addedTransaction);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(null);
        }
    }
}