package com.brlopes.Controller;

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
import com.brlopes.Service.exceptions.SameClientException;
import com.brlopes.Service.exceptions.TokenExpiredException;
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
            .map(this::mapToTransactionDTO)
            .collect(Collectors.toList());
            
            return ResponseEntity.ok().body(transactionDTOs);
        } catch (TokenExpiredException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token Inválido. Faça o Registo/Login primeiro para ver todas as transacções");
        }
    }
    
    private TransactionDTO mapToTransactionDTO(Transactions transaction) {
        return new TransactionDTO(
        transaction.getDate(),
        transaction.getTotalAmmount(),
        transaction.getClass().getName(),
        transaction.getClient().getAge()
        );
    }
    
    @PostMapping("/add")
    public ResponseEntity<Object> addTransaction(@RequestBody Transactions transaction, @RequestHeader("Authorization") String token) {
        try {
            Transactions newTransaction = transactionsService.insert(token, transaction);
            return ResponseEntity.ok(newTransaction);
        } catch (TokenExpiredException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT expirado. Faça o Registo/Login novamente para adicionar transacção.");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Um dos clientes não existe na base de dados");
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O cliente ou o cliente destino não podem ser nulos");
        } catch (SameClientException e ){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Os IDs do cliente e do cliente destino não podem ser iguais");
        }

    }
    
    
    
    @GetMapping("/{id}")
    public ResponseEntity<Transactions> findById(@PathVariable Long id) {
        Transactions transaction = transactionsService.findById(id);
        return ResponseEntity.ok().body(transaction);
    }
}