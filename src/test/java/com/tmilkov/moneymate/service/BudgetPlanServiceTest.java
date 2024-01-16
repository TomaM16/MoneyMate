package com.tmilkov.moneymate.service;

import com.tmilkov.moneymate.mapper.BudgetPlanMapper;
import com.tmilkov.moneymate.model.entity.budget.BudgetPlan;
import com.tmilkov.moneymate.model.entity.transaction.Transaction;
import com.tmilkov.moneymate.model.entity.transaction.TransactionCategory;
import com.tmilkov.moneymate.model.entity.transaction.TransactionType;
import com.tmilkov.moneymate.model.entity.user.Role;
import com.tmilkov.moneymate.model.entity.user.User;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

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

  private static List<User> mockUsers;
  private static List<TransactionCategory> mockTransactionCategories;
  private static List<Transaction> mockTransactions;
  private static List<BudgetPlan> mockBudgetPlans;

  @BeforeAll
  static void before() {
    mockUsers = List.of(
      new User(1L, "User 1", "Name 1", "email 1", "pass 1", Role.USER),
      new User(2L, "User 2", "Name 2", "email 2", "pass 2", Role.USER),
      new User(3L, "User 3", "Name 3", "email 3", "pass 3", Role.ADMIN)
    );

    mockTransactionCategories = List.of(
      new TransactionCategory(1L, mockUsers.get(0), "Category1", "Category1 description", Set.of()),
      new TransactionCategory(2L, mockUsers.get(1), "Category2", "Category2 description", Set.of()),
      new TransactionCategory(3L, mockUsers.get(1), "Category3", "Category3 description", Set.of())
    );

    mockTransactions = List.of(
      new Transaction(
        1L,
        new Date(),
        "Transaction1",
        new BigDecimal(200),
        TransactionType.INCOME,
        mockTransactionCategories.get(0),
        mockTransactionCategories.get(0).getUser()
      ),
      new Transaction(
        2L,
        new Date(),
        "Transaction2",
        new BigDecimal(20),
        TransactionType.EXPENSE,
        mockTransactionCategories.get(0),
        mockTransactionCategories.get(0).getUser()
      ),
      new Transaction(
        3L,
        new Date(),
        "Transaction3",
        new BigDecimal(100),
        TransactionType.INCOME,
        mockTransactionCategories.get(1),
        mockTransactionCategories.get(1).getUser()
      )
    );

    mockBudgetPlans = List.of(
      new BudgetPlan(
        1L,
        "BudgetPlan1",
        new Date(),
        Date.from(new Date().toInstant().plus(2, ChronoUnit.DAYS)),
        new BigDecimal(100),
        Set.of(),
        mockUsers.get(0)
      ),
      new BudgetPlan(
        2L,
        "BudgetPlan2",
        new Date(),
        Date.from(new Date().toInstant().plus(5, ChronoUnit.DAYS)),
        new BigDecimal(120),
        Set.of(),
        mockUsers.get(0)
      )
    );
  }

  private Authentication getAuthenticationForUser(User user) {
    return new UsernamePasswordAuthenticationToken(
      user,
      null,
      user.getAuthorities()
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

    final var mockUser = mockUsers.get(0);
    final var authentication = getAuthenticationForUser(mockUser);
    final var mockUserTransactions = mockTransactions.stream()
      .filter(transaction -> transaction.getUser().equals(mockUser))
      .toList();

    System.out.println(mockUserTransactions);

    when(transactionRepository.findAllByUserId(mockUser.getId())).thenReturn(mockUserTransactions);

    // when
    BudgetResponse budget = budgetPlanService.getBudgetByUser(authentication);

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

    final var mockUser = mockUsers.get(0);
    final var authentication = getAuthenticationForUser(mockUser);
    final var mockUserBudgetPlans = mockBudgetPlans.stream()
      .filter(budgetPlan -> budgetPlan.getUser().equals(mockUser))
      .toList();

    when(budgetPlanMapper.toResponse(any(BudgetPlan.class)))
      .thenAnswer(invocation -> {
        BudgetPlan inputBudgetPlan = invocation.getArgument(0);
        return expectedBudgetPlansResponses.stream()
          .filter(response -> response.getId().equals(inputBudgetPlan.getId()))
          .findFirst()
          .orElse(null);
      });
    when(budgetPlanRepository.findAllByUserId(mockUser.getId())).thenReturn(mockUserBudgetPlans);

    // when
    List<BudgetPlanResponse> budgetPlans = budgetPlanService.getAllBudgetPlansByUser(authentication);

    // then
    assertEquals(expectedBudgetPlansResponses, budgetPlans);
  }

  @Test
  public void testAddBudgetPlan() {
    // given
    final var category1 = new TransactionCategory(
      1L,
      new User(1L, "User 1", "Name 1", "email 1", "pass 1", Role.USER),
      "Category1",
      "Category1 description",
      Set.of()
    );
    final var category3 = new TransactionCategory(
      3L,
      new User(2L, "User 2", "Name 2", "email 2", "pass 2", Role.USER),
      "Category3",
      "Category3 description",
      Set.of()
    );

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

    final var mockUser = mockUsers.get(0);
    final var authentication = getAuthenticationForUser(mockUser);

    when(transactionCategoryRepository.findAllByIdInAndUserId(request.getTransactionCategoryIds(), mockUser.getId()))
      .thenReturn(List.of(category1, category3));
    when(budgetPlanMapper.toEntity(request, Set.of(category1, category3), mockUser)).thenReturn(budgetPlan);
    when(budgetPlanMapper.toResponse(budgetPlan)).thenReturn(expectedBudgetPlanResponse);
    when(budgetPlanRepository.save(budgetPlan)).thenReturn(budgetPlan);

    // when
    final BudgetPlanResponse budgetPlanResponse = budgetPlanService.addBudgetPlanForUser(request, authentication);

    // then
    assertEquals(expectedBudgetPlanResponse, budgetPlanResponse);
  }

}
