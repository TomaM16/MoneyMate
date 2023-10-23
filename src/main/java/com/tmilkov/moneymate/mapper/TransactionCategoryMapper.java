package com.tmilkov.moneymate.mapper;

import com.tmilkov.moneymate.model.entity.transaction.TransactionCategory;
import com.tmilkov.moneymate.model.request.TransactionCategoryRequest;
import com.tmilkov.moneymate.model.response.TransactionCategoryResponse;
import org.springframework.stereotype.Component;

@Component
public class TransactionCategoryMapper {
    public TransactionCategoryResponse toResponse(TransactionCategory category) {
        return new TransactionCategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }

    public TransactionCategory toEntity(TransactionCategoryRequest request) {
        return TransactionCategory.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
    }
}