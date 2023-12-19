package com.tmilkov.moneymate.mapper;

import com.tmilkov.moneymate.model.entity.budget.BudgetPlan;
import com.tmilkov.moneymate.model.entity.transaction.TransactionCategory;
import com.tmilkov.moneymate.model.request.BudgetPlanRequest;
import com.tmilkov.moneymate.model.response.BudgetPlanResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class BudgetPlanMapper {

  private final TransactionCategoryMapper transactionCategoryMapper;

  public BudgetPlanResponse toResponse(BudgetPlan budgetPlan) {
    return new BudgetPlanResponse(
      budgetPlan.getId(),
      budgetPlan.getName(),
      budgetPlan.getStartDate(),
      budgetPlan.getEndDate(),
      budgetPlan.getMonthlyBudget(),
      budgetPlan.getTransactionCategories().stream()
        .map(transactionCategoryMapper::toResponse)
        .collect(Collectors.toSet())
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
