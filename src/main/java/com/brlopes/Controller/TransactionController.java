package com.brlopes.Controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

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

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    
    @Autowired
    private TransactionsService transactionsService;
    
    @GetMapping("/") // Read - NORMAL CLIENT ROLE
    public ResponseEntity<?> findAll() {
        Iterable<Transactions> transactions = transactionsService.findAll();
        
        List<TransactionDTO> transactionList = StreamSupport.stream(transactions.spliterator(), false)
        .map(this::mapToDTO)
        .toList();
        
        return ResponseEntity.ok().body(transactionList);
    }
    
    private TransactionDTO mapToDTO(Transactions transaction) {
        return new TransactionDTO(
        transaction.getTotalAmmount(),
        transaction.getClient().getName(),
        transaction.getDestinyClient().getName(),
        transaction.getDate().toString()
        );
    }
    
    @PostMapping("/add")
    public ResponseEntity<?> addTransaction(@RequestBody TransactionDTO request) {
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
    
    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String message) {
        ErrorResponse errorResponse = new ErrorResponse(status, message);
        return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
    }
    
    @GetMapping("/{id}") // Read (by ID) - ADMIN ROLE
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Transactions transaction = transactionsService.findById(id);
        return ResponseEntity.ok().body(transaction);
    }
    
    @DeleteMapping("/{id}") // Delete (by ID) - ADMIN ROLE
    public ResponseEntity<ErrorResponse> deletebyId(@PathVariable Long id) {
        transactionsService.deletebyId(id);
        return ResponseEntity.noContent().build();
        
    }
    
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