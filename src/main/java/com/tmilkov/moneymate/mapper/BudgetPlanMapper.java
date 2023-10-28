package com.tmilkov.moneymate.mapper;

import com.tmilkov.moneymate.model.entity.budget.BudgetPlan;
import com.tmilkov.moneymate.model.entity.transaction.TransactionCategory;
import com.tmilkov.moneymate.model.request.BudgetPlanRequest;
import com.tmilkov.moneymate.model.response.BudgetPlanResponse;
import org.springframework.stereotype.Component;

import java.util.Set;


@Component
public class BudgetPlanMapper {
    public BudgetPlanResponse toResponse(BudgetPlan budgetPlan) {
        return new BudgetPlanResponse(
                budgetPlan.getId(),
                budgetPlan.getName(),
                budgetPlan.getStartDate(),
                budgetPlan.getEndDate(),
                budgetPlan.getMonthlyBudget()
        );
    }

    public BudgetPlan toEntity(BudgetPlanRequest request, Set<TransactionCategory> transactionCategories) {
        return BudgetPlan.builder()
                .name(request.getName())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .monthlyBudget(request.getMonthlyBudget())
                .transactionCategories(transactionCategories)
                .build();
    }
}