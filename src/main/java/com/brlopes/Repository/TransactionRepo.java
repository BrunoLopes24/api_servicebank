package com.brlopes.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.brlopes.Model.Transactions;

/**
 * The TransactionRepo interface extends JpaRepository.
 * It provides CRUD operations for the Transactions entity.
 */
public interface TransactionRepo extends JpaRepository<Transactions, Long>{

}