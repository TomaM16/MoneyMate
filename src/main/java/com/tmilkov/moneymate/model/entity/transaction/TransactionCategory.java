package com.tmilkov.moneymate.model.entity.transaction;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tmilkov.moneymate.model.entity.budget.BudgetPlan;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.*;

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

//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;

    private String name;
    private String description;

    @JsonManagedReference
    @ManyToMany(mappedBy = "transactionCategories")
    private Set<BudgetPlan> budgetPlans = new HashSet<>();

}