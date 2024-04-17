package com.brlopes.Controller;

import java.util.List;
import java.util.stream.Collectors;
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
        .map(transaction -> new TransactionDTO(
        transaction.getDate(),
        transaction.getTotalAmmount(),
        transaction.getDestinyClient()))
        .collect(Collectors.toList());
        
        return ResponseEntity.ok().body(transactionList);
    }
    
    
    @PostMapping("/add") // Create - NORMAL CLIENT ROLE
    public ResponseEntity<ErrorResponse> addTransaction(@RequestBody Transactions transaction) {
        try {
            transactionsService.insert(transaction);
            return ResponseEntity.ok().build();
        } catch (DataIntegrityViolationException e) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, "One of the customers does not exist in the database");
            return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
        } catch (IllegalArgumentException e){
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, "The client or target client cannot be null");
            return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
        } catch (SameClientException e ){
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, "Client and target Client IDs cannot be the same");
            return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
        }
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
}