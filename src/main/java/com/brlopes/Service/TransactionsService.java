package com.brlopes.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.brlopes.Model.Transactions;
import com.brlopes.Repository.ClientRepo;
import com.brlopes.Repository.TransactionRepo;
import com.brlopes.Service.exceptions.DataIntegrityViolationException;
import com.brlopes.Service.exceptions.ResourceNotFoundException;
import com.brlopes.Service.exceptions.TokenExpiredException;

@Service
public class TransactionsService {
    
    @Autowired
    private TransactionRepo transactionRepo;
    @Autowired
    private ClientRepo clientRepo;
    
    
    public Transactions insert(String token, Transactions transaction) {
        try {
            verifyToken(token);
            
            // Salvando a transação na base de dados
            transaction = transactionRepo.save(transaction);
            
            // Verificando se o cliente existe na base de dados
            Long clientId = transaction.getClient().getClient_id();
            if (!clientRepo.existsById(clientId)) {
                // Se o cliente não existir, lançar uma exceção com mensagem específica
                throw new DataIntegrityViolationException("O cliente não existe na base de dados");
            }
            // Retornar a transação após a inserção
            return transaction;
        } catch (com.auth0.jwt.exceptions.TokenExpiredException e) {
            // Lidar com o token expirado
            throw new TokenExpiredException("Token JWT expirado. Faça o Registo/Login novamente para adicionar transacção.");
        } catch (DataIntegrityViolationException e) {
            // Se houver uma violação de integridade de dados, lançar uma exceção específica
            throw e;
        }
    }
    
    
    private void verifyToken(String token) {
        // Verificar o token JWT usando a mesma chave secreta que foi usada para criar o token
        try {
            Algorithm algorithm = Algorithm.HMAC256("servicebank");
            JWT.require(algorithm).build().verify(token);
        } catch (TokenExpiredException e) {
            // Lançar exceção se o token estiver expirado
            throw new TokenExpiredException("Token JWT expirado. Faça o Registo/Login novamente para adicionar transacção");
        }
    }
    
    
    
    public Iterable<Transactions> findAll(String token){ // Read
        // Aqui você pode adicionar lógica de verificação de token, se necessário
        verifyToken(token);
        return transactionRepo.findAll();
    }
    
    public Transactions findById(Long id){
        Optional<Transactions> transaction = transactionRepo.findById(id);
        
        return transaction.orElseThrow(()-> new ResourceNotFoundException(id));
    }
    
}