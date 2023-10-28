package com.tmilkov.moneymate.service.budget;

import com.tmilkov.moneymate.mapper.BudgetPlanMapper;
import com.tmilkov.moneymate.model.entity.transaction.TransactionCategory;
import com.tmilkov.moneymate.model.request.BudgetPlanRequest;
import com.tmilkov.moneymate.model.response.BudgetPlanResponse;
import com.tmilkov.moneymate.repository.budget.BudgetPlanRepository;
import com.tmilkov.moneymate.repository.transaction.TransactionCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BudgetPlanService {

    private final TransactionCategoryRepository transactionCategoryRepository;
    private final BudgetPlanRepository budgetPlanRepository;
    private final BudgetPlanMapper budgetPlanMapper;

    public List<BudgetPlanResponse> getAllBudgetPlansByCategory(Long categoryId) throws NoSuchElementException {
        final var category = transactionCategoryRepository.findById(categoryId).orElseThrow();

        return category.getBudgetPlans()
                .stream()
                .map(budgetPlanMapper::toResponse)
                .toList();
    }

    public BudgetPlanResponse addBudgetPlan(BudgetPlanRequest request) {
        final var newBudgetPlan = budgetPlanMapper.toEntity(request, new HashSet<>());
        final var budgetPlan = budgetPlanRepository.save(newBudgetPlan);
        final var transactionCategories = transactionCategoryRepository.findAllById(request.getTransactionCategoryIds());
        final Iterable<TransactionCategory> newTransactionCategories = transactionCategories
                .stream()
                .peek(transactionCategory -> transactionCategory.addBudgetPlan(budgetPlan))
                .toList();

        transactionCategoryRepository.saveAll(newTransactionCategories);

        return budgetPlanMapper.toResponse(budgetPlan);
    }

}
