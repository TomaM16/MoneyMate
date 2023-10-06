package com.tmilkov.moneymate.repository.transaction;

import com.tmilkov.moneymate.model.entity.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
