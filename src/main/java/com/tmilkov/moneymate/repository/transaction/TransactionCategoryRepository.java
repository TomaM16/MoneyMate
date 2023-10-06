package com.tmilkov.moneymate.repository.transaction;

import com.tmilkov.moneymate.model.entity.transaction.TransactionCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionCategoryRepository extends JpaRepository<TransactionCategory, Long> {
}
