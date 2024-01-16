package com.tmilkov.moneymate.model.entity.transaction;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tmilkov.moneymate.model.entity.budget.BudgetPlan;
import com.tmilkov.moneymate.model.entity.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TransactionCategory {

  @Id
  @GeneratedValue
  private Long id;

  @JsonBackReference
  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  private String name;
  private String description;

  @JsonManagedReference
  @ManyToMany(mappedBy = "transactionCategories")
  private Set<BudgetPlan> budgetPlans = new HashSet<>();

}
