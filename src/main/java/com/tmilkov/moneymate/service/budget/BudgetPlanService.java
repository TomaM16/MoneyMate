package com.tmilkov.moneymate.service.budget;

import com.tmilkov.moneymate.mapper.BudgetPlanMapper;
import com.tmilkov.moneymate.model.entity.transaction.Transaction;
import com.tmilkov.moneymate.model.entity.transaction.TransactionType;
import com.tmilkov.moneymate.model.request.BudgetPlanRequest;
import com.tmilkov.moneymate.model.response.BudgetPlanResponse;
import com.tmilkov.moneymate.model.response.BudgetResponse;
import com.tmilkov.moneymate.repository.budget.BudgetPlanRepository;
import com.tmilkov.moneymate.repository.transaction.TransactionCategoryRepository;
import com.tmilkov.moneymate.repository.transaction.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetPlanService {

    private final TransactionCategoryRepository transactionCategoryRepository;
    private final TransactionRepository transactionRepository;
    private final BudgetPlanRepository budgetPlanRepository;
    private final BudgetPlanMapper budgetPlanMapper;

    public BudgetResponse getBudget() {
        final var transactions = transactionRepository.findAll();
        final var balance = transactions.stream()
                .map(transaction -> transaction.getType() == TransactionType.INCOME ?
                        transaction.getAmount() :
                        transaction.getAmount().negate())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        final var expensesMonth = getTransactionsAmount(transactions, TransactionType.EXPENSE);
        final var incomeMonth = getTransactionsAmount(transactions, TransactionType.INCOME);

        final var used = expensesMonth
                .divide(BigDecimal.ZERO.equals(incomeMonth) ? BigDecimal.ONE : incomeMonth, 10, RoundingMode.HALF_EVEN)
                .multiply(new BigDecimal(100))
                .abs().intValue();

        return new BudgetResponse(used, balance, incomeMonth, expensesMonth);
    }

    private BigDecimal getTransactionsAmount(List<Transaction> transactions, TransactionType type) {
        return transactions.stream()
                .filter(transaction -> {
                            LocalDate localDate = transaction.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                            return localDate.getMonth() == java.time.LocalDate.now().getMonth();
                        }
                )
                .filter(transaction -> transaction.getType().equals(type))
                .map(Transaction::getAmount)
                .reduce(new BigDecimal(0), BigDecimal::add);
    }

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
