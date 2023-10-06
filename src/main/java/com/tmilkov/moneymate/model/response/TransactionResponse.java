package com.tmilkov.moneymate.model.response;

import com.tmilkov.moneymate.model.entity.transaction.TransactionCategory;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
public class TransactionResponse {
    private final Date date;
    private final String description;
    private final BigDecimal amount;
    private final TransactionCategory category;
}
