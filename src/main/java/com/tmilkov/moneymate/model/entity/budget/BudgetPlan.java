package com.tmilkov.moneymate.model.entity.budget;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.tmilkov.moneymate.model.entity.transaction.TransactionCategory;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BudgetPlan {

  @Id
  @GeneratedValue
  private Long id;

  private String name;
  private Date startDate;
  private Date endDate;
  private BigDecimal monthlyBudget;

  @JsonBackReference
  @ManyToMany
  @NotEmpty(message = "At least one category is required")
  @JoinTable(name = "TransactionCategory_BudgetPlan",
    joinColumns = @JoinColumn(name = "plan_id"),
    inverseJoinColumns = @JoinColumn(name = "category_id"))
  private Set<TransactionCategory> transactionCategories;
}
