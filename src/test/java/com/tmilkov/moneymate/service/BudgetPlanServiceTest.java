package com.tmilkov.moneymate.service;

import com.tmilkov.moneymate.mapper.BudgetPlanMapper;
import com.tmilkov.moneymate.model.entity.budget.BudgetPlan;
import com.tmilkov.moneymate.model.entity.transaction.Transaction;
import com.tmilkov.moneymate.model.entity.transaction.TransactionCategory;
import com.tmilkov.moneymate.model.entity.transaction.TransactionType;
import com.tmilkov.moneymate.model.request.BudgetPlanRequest;
import com.tmilkov.moneymate.model.response.BudgetPlanResponse;
import com.tmilkov.moneymate.model.response.BudgetResponse;
import com.tmilkov.moneymate.model.response.TransactionCategoryResponse;
import com.tmilkov.moneymate.repository.budget.BudgetPlanRepository;
import com.tmilkov.moneymate.repository.transaction.TransactionCategoryRepository;
import com.tmilkov.moneymate.repository.transaction.TransactionRepository;
import com.tmilkov.moneymate.service.budget.BudgetPlanService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BudgetPlanServiceTest {
  @Mock
  private TransactionCategoryRepository transactionCategoryRepository;
  @Mock
  private TransactionRepository transactionRepository;
  @Mock
  private BudgetPlanRepository budgetPlanRepository;
  @Mock
  private BudgetPlanMapper budgetPlanMapper;

  @InjectMocks
  private BudgetPlanService budgetPlanService;

  private static List<TransactionCategory> mockTransactionCategories;
  private static List<Transaction> mockTransactions;
  private static List<BudgetPlan> mockBudgetPlans;

  @BeforeAll
  static void before() {
    mockTransactionCategories = List.of(
      new TransactionCategory(1L, "Category1", "Category1 description", Set.of()),
      new TransactionCategory(2L, "Category2", "Category2 description", Set.of()),
      new TransactionCategory(3L, "Category3", "Category3 description", Set.of())
    );
    mockTransactions = List.of(
      new Transaction(
        1L,
        new Date(),
        "Transaction1",
        new BigDecimal(100),
        TransactionType.INCOME,
        new TransactionCategory(1L, "Category1", "Category1 description", Set.of())
      ),
      new Transaction(
        2L,
        new Date(),
        "Transaction2",
        new BigDecimal(100),
        TransactionType.INCOME,
        new TransactionCategory(2L, "Category1", "Category1 description", Set.of())
      ),
      new Transaction(
        3L,
        new Date(),
        "Transaction3",
        new BigDecimal(20),
        TransactionType.EXPENSE,
        new TransactionCategory(3L, "Category1", "Category1 description", Set.of())
      )
    );
    mockBudgetPlans = List.of(
      new BudgetPlan(
        1L,
        "BudgetPlan1",
        new Date(),
        Date.from(new Date().toInstant().plus(2, ChronoUnit.DAYS)),
        new BigDecimal(100),
        Set.of()
      ),
      new BudgetPlan(
        2L,
        "BudgetPlan2",
        new Date(),
        Date.from(new Date().toInstant().plus(5, ChronoUnit.DAYS)),
        new BigDecimal(120),
        Set.of()
      )
    );
  }

  @Test
  public void testGetBudget() {
    // given
    final var expectedBudgetResponse = new BudgetResponse(
      10,
      new BigDecimal(180),
      new BigDecimal(200),
      new BigDecimal(20)
    );

    when(transactionRepository.findAll()).thenReturn(mockTransactions);

    // when
    BudgetResponse budget = budgetPlanService.getBudget();

    // then
    assertEquals(expectedBudgetResponse, budget);
  }

  @Test
  public void testGetAllBudgetPlans() {
    // given
    final var expectedBudgetPlansResponses = List.of(
      new BudgetPlanResponse(
        1L,
        "BudgetPlan1",
        new Date(),
        Date.from(new Date().toInstant().plus(2, ChronoUnit.DAYS)),
        new BigDecimal(100),
        Set.of()
      ),
      new BudgetPlanResponse(
        2L,
        "BudgetPlan2",
        new Date(),
        Date.from(new Date().toInstant().plus(5, ChronoUnit.DAYS)),
        new BigDecimal(120),
        Set.of()
      )
    );

    when(budgetPlanMapper.toResponse(any(BudgetPlan.class)))
      .thenAnswer(invocation -> {
        BudgetPlan inputBudgetPlan = invocation.getArgument(0);
        return expectedBudgetPlansResponses.stream()
          .filter(response -> response.getId().equals(inputBudgetPlan.getId()))
          .findFirst()
          .orElse(null);
      });
    when(budgetPlanRepository.findAll()).thenReturn(mockBudgetPlans);

    // when
    List<BudgetPlanResponse> budgetPlans = budgetPlanService.getAllBudgetPlans();

    // then
    assertEquals(expectedBudgetPlansResponses, budgetPlans);
  }

  @Test
  public void testAddBudgetPlan() {
    // given
    final var category1 = new TransactionCategory(1L, "Category1", "Category1 description", Set.of());
    final var category3 = new TransactionCategory(3L, "Category3", "Category3 description", Set.of());

    final var request = new BudgetPlanRequest(
      "BudgetPlan1",
      new Date(),
      Date.from(new Date().toInstant().plus(5, ChronoUnit.DAYS)),
      new BigDecimal(100),
      Set.of(category1.getId(), category3.getId())
    );
    final var expectedCategory1Response = new TransactionCategoryResponse(1L, "Category1", "Category1 description");
    final var expectedCategory3Response = new TransactionCategoryResponse(3L, "Category3", "Category3 description");
    final var expectedBudgetPlanResponse = new BudgetPlanResponse(
      1L,
      "BudgetPlan1",
      new Date(),
      Date.from(new Date().toInstant().plus(5, ChronoUnit.DAYS)),
      new BigDecimal(100),
      Set.of(expectedCategory1Response, expectedCategory3Response)
    );

    final var budgetPlan = BudgetPlan.builder()
      .name(request.getName())
      .startDate(request.getStartDate())
      .endDate(request.getEndDate())
      .monthlyBudget(request.getMonthlyBudget())
      .transactionCategories(
        Set.copyOf(
          mockTransactionCategories.stream()
            .filter(category -> request.getTransactionCategoryIds().contains(category.getId()))
            .collect(Collectors.toList())
        )
      )
      .build();

    when(transactionCategoryRepository.findAllById(request.getTransactionCategoryIds()))
      .thenReturn(List.of(category1, category3));
    when(budgetPlanMapper.toEntity(request, Set.of(category1, category3))).thenReturn(budgetPlan);
    when(budgetPlanMapper.toResponse(budgetPlan)).thenReturn(expectedBudgetPlanResponse);
    when(budgetPlanRepository.save(budgetPlan)).thenReturn(budgetPlan);

    // when
    final BudgetPlanResponse budgetPlanResponse = budgetPlanService.addBudgetPlan(request);

    // then
    assertEquals(expectedBudgetPlanResponse, budgetPlanResponse);


//        final var transactionCategories = transactionCategoryRepository.findAllById(request.getTransactionCategoryIds());
//        final var newBudgetPlan = budgetPlanMapper.toEntity(request, new HashSet<>(transactionCategories));
//
//        return budgetPlanMapper.toResponse(budgetPlanRepository.save(newBudgetPlan));
  }

}
