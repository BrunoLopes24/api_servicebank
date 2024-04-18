package com.brlopes.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brlopes.Model.Transactions;

public interface TransactionRepo extends JpaRepository<Transactions, Long>{
    
}
