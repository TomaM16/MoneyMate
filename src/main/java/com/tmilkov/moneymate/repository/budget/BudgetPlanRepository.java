package com.tmilkov.moneymate.repository.budget;

import com.tmilkov.moneymate.model.entity.budget.BudgetPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BudgetPlanRepository extends JpaRepository<BudgetPlan, Long> {

  List<BudgetPlan> findAllByUserId(Long userId);

}
