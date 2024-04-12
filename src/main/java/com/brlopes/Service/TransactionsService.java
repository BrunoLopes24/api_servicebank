package com.brlopes.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.brlopes.Model.Client;
import com.brlopes.Model.Transactions;
import com.brlopes.Repository.ClientRepo;
import com.brlopes.Repository.TransactionRepo;

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
            Client client = transaction.getClient();
            if (clientRepo.findById(client.getClient_id()).isEmpty()) {
                // Se o cliente não existir, lançar uma exceção
                throw new DataIntegrityViolationException("O cliente não existe na base de dados");
            }
            
            // Se o token for válido e o cliente existir, retornar a transação
            return transaction;
        } catch (JWTVerificationException e) {
            // Se o token for inválido, lançar uma exceção ou lidar com ela de acordo com suas necessidades
            throw new RuntimeException("Token JWT inválido", e); // Lançar uma exceção genérica
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Não é possível adicionar uma transação sem um cliente na base de dados");
        }
    }
    
    
    private void verifyToken(String token) {
        // Verificar o token JWT usando a mesma chave secreta que foi usada para criar o token
        Algorithm algorithm = Algorithm.HMAC256("servicebank");
        JWT.require(algorithm).build().verify(token);
    }
    
    
    public Iterable<Transactions> findAll(String token){ // Read
        // Aqui você pode adicionar lógica de verificação de token, se necessário
        verifyToken(token);
        return transactionRepo.findAll();
    }
    
}