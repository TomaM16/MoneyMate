package com.tmilkov.moneymate.mapper;

import com.tmilkov.moneymate.model.entity.transaction.Transaction;
import com.tmilkov.moneymate.model.entity.transaction.TransactionCategory;
import com.tmilkov.moneymate.model.request.TransactionRequest;
import com.tmilkov.moneymate.model.response.TransactionResponse;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {
    public TransactionResponse toResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getDate(),
                transaction.getDescription(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getCategory()
        );
    }

    public Transaction toEntity(TransactionRequest request, TransactionCategory category) {
        return Transaction.builder()
                .date(request.getDate())
                .description(request.getDescription())
                .amount(request.getAmount())
                .type(request.getType())
                .category(category)
                .build();
    }
}