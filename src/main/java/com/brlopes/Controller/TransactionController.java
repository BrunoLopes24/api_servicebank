package com.brlopes.Controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.brlopes.Model.Transactions;
import com.brlopes.Service.TransactionsService;
import com.brlopes.dto.TransactionDTO;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    
    @Autowired
    private TransactionsService transactionsService;
    
    @GetMapping("/")
    public ResponseEntity<?> findAllTransactions(@RequestHeader(name = "Authorization", required = false) String token) {
        try {
            Iterable<Transactions> transactions = transactionsService.findAll(token);
            
            List<TransactionDTO> transactionDTOs = StreamSupport.stream(transactions.spliterator(), false)
            .map(transaction -> new TransactionDTO(transaction.getDate(), transaction.getTotalAmmount(),
            transaction.getClient().getName(), transaction.getClient().getAge()))
            .collect(Collectors.toList());
            return ResponseEntity.ok().body(transactionDTOs);
        } catch (JWTVerificationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Make Login first");
        }
    }
    
    
    @PostMapping("/add") // Create
    public ResponseEntity<?> addTransaction(@RequestHeader("Authorization") String token, @RequestBody Transactions transaction) {
        transaction = transactionsService.insert(token, transaction);
        return ResponseEntity.ok().body(transaction);
    }
    
}