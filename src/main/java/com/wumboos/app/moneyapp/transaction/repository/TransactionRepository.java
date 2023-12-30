package com.wumboos.app.moneyapp.transaction.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wumboos.app.moneyapp.transaction.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID>{
	
}
