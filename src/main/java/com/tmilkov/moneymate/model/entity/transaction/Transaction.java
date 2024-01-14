package com.tmilkov.moneymate.model.entity.transaction;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.tmilkov.moneymate.model.entity.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

  @JsonBackReference
  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @Override
  public int compareTo(Transaction o) {
    return getDate().compareTo(o.getDate());
  }
}

