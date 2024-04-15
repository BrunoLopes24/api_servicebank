package com.brlopes.Controller;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brlopes.Model.Transactions;
import com.brlopes.Service.TransactionsService;
import com.brlopes.Service.exceptions.DataIntegrityViolationException;
import com.brlopes.Service.exceptions.ErrorResponse;
import com.brlopes.Service.exceptions.SameClientException;
import com.brlopes.Service.exceptions.TokenExpiredException;
import com.brlopes.dto.TransactionDTO;



@RestController
@RequestMapping("/transactions")
public class TransactionController {
    
    @Autowired
    private TransactionsService transactionsService;
    
    @GetMapping("/") // Read
    public ResponseEntity<?> findAll(@RequestHeader("Authorization") String token) {
        try {
            Iterable<Transactions> transactions = transactionsService.findAll(token);
            
            List<TransactionDTO> transactionList = StreamSupport.stream(transactions.spliterator(), false)
            .map(transaction -> new TransactionDTO(
            transaction.getDate(),
            transaction.getTotalAmmount(),
            transaction.getDestinyClient()))
            .collect(Collectors.toList());
            
            return ResponseEntity.ok().body(transactionList);
        } catch (TokenExpiredException e) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid Token. Please Register/Login first to view all transactions");
            return ResponseEntity.status(errorResponse.getStatus()).body(Collections.singletonList(errorResponse));
        }
    }
    
    
    @PostMapping("/add")
    public ResponseEntity<?> addTransaction(@RequestBody Transactions transaction, @RequestHeader("Authorization") String token) {
        try {
            Transactions newTransaction = transactionsService.insert(token, transaction);
            return ResponseEntity.ok(newTransaction);
        } catch (TokenExpiredException e) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid Token. Please Register/Login first to add transactions");
            return ResponseEntity.status(errorResponse.getStatus()).body(Collections.singletonList(errorResponse));
        } catch (DataIntegrityViolationException e) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, "One of the customers does not exist in the database");
            return ResponseEntity.status(errorResponse.getStatus()).body(Collections.singletonList(errorResponse));
        } catch (IllegalArgumentException e){
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, "The client or target client cannot be null");
            return ResponseEntity.status(errorResponse.getStatus()).body(Collections.singletonList(errorResponse));
        } catch (SameClientException e ){
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, "Client and target Client IDs cannot be the same");
            return ResponseEntity.status(errorResponse.getStatus()).body(Collections.singletonList(errorResponse));
        }
    }
    
    
    
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        try {
            Transactions transaction = transactionsService.findById(token, id);
            return ResponseEntity.ok().body(transaction);
        } catch (TokenExpiredException e) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid Token. Please Register/Login first to check Client ID transactions");
            return ResponseEntity.status(errorResponse.getStatus()).body(Collections.singletonList(errorResponse));
        }  
    }
}