package com.tmilkov.moneymate.model.entity.transaction;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Transaction implements Comparable<Transaction> {

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

    @Override
    public int compareTo(Transaction o) {
        return getDate().compareTo(o.getDate());
    }
}

