package com.tmilkov.moneymate.repository.budget;

import com.tmilkov.moneymate.model.entity.budget.BudgetPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetPlanRepository extends JpaRepository<BudgetPlan, Long> {
}
