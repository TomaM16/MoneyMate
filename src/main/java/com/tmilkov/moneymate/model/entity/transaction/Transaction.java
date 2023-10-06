package com.tmilkov.moneymate.model.entity.transaction;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Transaction {

    @Id
    @GeneratedValue
    private Long id;

    private Date date;
    private String description;
    private BigDecimal amount;
    private TransactionType type;

    @ManyToOne
    @JoinColumn(name = "transaction_category_id")
    private TransactionCategory category;
}

