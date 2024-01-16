package com.tmilkov.moneymate.repository.transaction;

import com.tmilkov.moneymate.model.entity.transaction.TransactionCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TransactionCategoryRepository extends JpaRepository<TransactionCategory, Long> {

  Optional<TransactionCategory> findByIdAndUserId(Long id, Long userId);

  List<TransactionCategory> findAllByUserId(Long userId);

  List<TransactionCategory> findAllByIdInAndUserId(Set<Long> categoryIds, Long userId);

}
