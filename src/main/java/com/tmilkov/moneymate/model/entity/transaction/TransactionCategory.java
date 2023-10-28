package com.tmilkov.moneymate.model.entity.transaction;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.tmilkov.moneymate.model.entity.budget.BudgetPlan;
import jakarta.persistence.*;
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

    @JsonBackReference
    @ManyToMany
    @JoinTable(name = "TransactionCategory_BudgetPlan",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "plan_id"))
    private Set<BudgetPlan> budgetPlans = new HashSet<>();

    public void addBudgetPlan(BudgetPlan budgetPlan) {
        budgetPlans.add(budgetPlan);
    }

}