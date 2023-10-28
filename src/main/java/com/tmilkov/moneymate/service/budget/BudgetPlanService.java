package com.tmilkov.moneymate.service.budget;

import com.tmilkov.moneymate.mapper.BudgetPlanMapper;
import com.tmilkov.moneymate.model.request.BudgetPlanRequest;
import com.tmilkov.moneymate.model.response.BudgetPlanResponse;
import com.tmilkov.moneymate.repository.budget.BudgetPlanRepository;
import com.tmilkov.moneymate.repository.transaction.TransactionCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetPlanService {

    private final TransactionCategoryRepository transactionCategoryRepository;
    private final BudgetPlanRepository budgetPlanRepository;
    private final BudgetPlanMapper budgetPlanMapper;

    public List<BudgetPlanResponse> getAllBudgetPlans() {
        return budgetPlanRepository.findAll()
                .stream()
                .map(budgetPlanMapper::toResponse)
                .toList();
    }

    public BudgetPlanResponse addBudgetPlan(BudgetPlanRequest request) {
        final var transactionCategories = transactionCategoryRepository.findAllById(request.getTransactionCategoryIds());
        final var newBudgetPlan = budgetPlanMapper.toEntity(request, new HashSet<>(transactionCategories));

        return budgetPlanMapper.toResponse(budgetPlanRepository.save(newBudgetPlan));
    }

}
