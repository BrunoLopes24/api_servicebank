package com.brlopes.Repository;

import org.springframework.data.repository.CrudRepository;

import com.brlopes.Model.Transactions;

public interface TransactionRepo extends CrudRepository<Transactions, Long>{
    
}
