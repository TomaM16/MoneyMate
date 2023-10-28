package com.tmilkov.moneymate.model.entity.budget;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tmilkov.moneymate.model.entity.transaction.TransactionCategory;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.*;

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

    @JsonManagedReference
    @ManyToMany(mappedBy = "budgetPlans")
    private Set<TransactionCategory> transactionCategories;
}
