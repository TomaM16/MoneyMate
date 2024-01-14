package com.tmilkov.moneymate.repository.transaction;

import com.tmilkov.moneymate.model.entity.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

  Optional<Transaction> findByIdAndUserId(Long id, Long userId);

  List<Transaction> findAllByUserId(Long userId);

  void deleteByIdAndUserId(Long id, Long userId);

}
