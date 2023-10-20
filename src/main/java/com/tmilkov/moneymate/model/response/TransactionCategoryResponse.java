package com.tmilkov.moneymate.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionCategoryResponse {
    private final Long id;
    private final String name;
    private final String description;
}
