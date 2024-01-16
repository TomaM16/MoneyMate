package com.tmilkov.moneymate.mapper;

import com.tmilkov.moneymate.model.entity.transaction.Transaction;
import com.tmilkov.moneymate.model.entity.transaction.TransactionCategory;
import com.tmilkov.moneymate.model.entity.user.User;
import com.tmilkov.moneymate.model.request.TransactionRequest;
import com.tmilkov.moneymate.model.response.TransactionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionMapper {

  private final TransactionCategoryMapper transactionCategoryMapper;

  public TransactionResponse toResponse(Transaction transaction) {
    return new TransactionResponse(
      transaction.getId(),
      transaction.getDate(),
      transaction.getDescription(),
      transaction.getAmount(),
      transaction.getType(),
      transactionCategoryMapper.toResponse(transaction.getCategory())
    );
  }

  public Transaction toEntity(TransactionRequest request, TransactionCategory category, User user) {
    return Transaction.builder()
      .date(request.getDate())
      .description(request.getDescription())
      .amount(request.getAmount())
      .type(request.getType())
      .category(category)
      .user(user)
      .build();
  }
}
