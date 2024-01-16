package com.tmilkov.moneymate.service;

import com.tmilkov.moneymate.mapper.TransactionMapper;
import com.tmilkov.moneymate.model.entity.transaction.Transaction;
import com.tmilkov.moneymate.model.entity.transaction.TransactionCategory;
import com.tmilkov.moneymate.model.entity.transaction.TransactionType;
import com.tmilkov.moneymate.model.entity.user.Role;
import com.tmilkov.moneymate.model.entity.user.User;
import com.tmilkov.moneymate.model.request.TransactionRequest;
import com.tmilkov.moneymate.model.response.TransactionCategoryResponse;
import com.tmilkov.moneymate.model.response.TransactionResponse;
import com.tmilkov.moneymate.repository.transaction.TransactionCategoryRepository;
import com.tmilkov.moneymate.repository.transaction.TransactionRepository;
import com.tmilkov.moneymate.service.transaction.TransactionsService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class TransactionsServiceTest {

  @Mock
  private TransactionCategoryRepository transactionCategoryRepository;
  @Mock
  private TransactionRepository transactionRepository;

  @Mock
  private TransactionMapper transactionMapper;

  @InjectMocks
  private TransactionsService transactionsService;

  private static List<User> mockUsers;
  private static List<TransactionCategory> mockTransactionCategories;
  private static List<Transaction> mockTransactions;

  @BeforeAll
  static void before() {
    mockUsers = List.of(
      new User(1L, "User 1", "Name 1", "email 1", "pass 1", Role.USER),
      new User(2L, "User 2", "Name 2", "email 2", "pass 2", Role.USER),
      new User(3L, "User 3", "Name 3", "email 3", "pass 3", Role.ADMIN)
    );

    mockTransactionCategories = List.of(
      new TransactionCategory(1L, mockUsers.get(0), "Category1", "Category1 description", Set.of()),
      new TransactionCategory(2L, mockUsers.get(0), "Category2", "Category2 description", Set.of()),
      new TransactionCategory(3L, mockUsers.get(1), "Category3", "Category3 description", Set.of())
    );
    mockTransactions = List.of(
      new Transaction(
        1L,
        new Date(),
        "Transaction1",
        new BigDecimal("100.0"),
        TransactionType.INCOME,
        mockTransactionCategories.get(0),
        mockTransactionCategories.get(0).getUser()
      ),
      new Transaction(
        2L,
        new Date(),
        "Transaction2",
        new BigDecimal("100.0"),
        TransactionType.INCOME,
        mockTransactionCategories.get(0),
        mockTransactionCategories.get(0).getUser()
      ),
      new Transaction(
        3L,
        new Date(),
        "Transaction3",
        new BigDecimal("100.0"),
        TransactionType.INCOME,
        mockTransactionCategories.get(1),
        mockTransactionCategories.get(1).getUser()
      )
    );
  }

  @Test
  public void testGetAllTransactions() {
    // given
    final var expectedTransactionResponses = List.of(
      new TransactionResponse(
        1L,
        new Date(),
        "Transaction1",
        new BigDecimal("100.0"),
        TransactionType.INCOME,
        new TransactionCategoryResponse(
          1L,
          "Category1",
          "Category1 description"
        )
      ),
      new TransactionResponse(
        2L,
        new Date(),
        "Transaction2",
        new BigDecimal("100.0"),
        TransactionType.INCOME,
        new TransactionCategoryResponse(2L, "Category2", "Category2 description")
      ),
      new TransactionResponse(
        3L,
        new Date(),
        "Transaction3",
        new BigDecimal("100.0"),
        TransactionType.INCOME,
        new TransactionCategoryResponse(3L, "Category3", "Category3 description")
      )
    );

    final var mockUser = mockUsers.get(0);
    Authentication authentication = getAuthenticationForUser(mockUser);

    when(transactionMapper.toResponse(any(Transaction.class)))
      .thenAnswer(invocation -> {
        Transaction inputTransaction = invocation.getArgument(0);
        return expectedTransactionResponses.stream()
          .filter(response -> response.getId().equals(inputTransaction.getId()))
          .findFirst()
          .orElse(null);
      });

    when(transactionRepository.findAllByUserId(mockUser.getId())).thenReturn(mockTransactions);

    // when
    List<TransactionResponse> transactions = transactionsService.getAllTransactionsByUser(authentication);

    // then
    assertEquals(expectedTransactionResponses, transactions);
  }

  private Authentication getAuthenticationForUser(User user) {
    return new UsernamePasswordAuthenticationToken(
      user,
      null,
      user.getAuthorities()
    );
  }

  @Test
  public void testGetTransaction() {
    // given
    final Long id = 1L;
    final var transaction = mockTransactions.stream()
      .filter(t -> t.getId().equals(id))
      .findFirst();
    final var expectedTransactionResponse = new TransactionResponse(
      1L,
      new Date(),
      "Transaction1",
      new BigDecimal("100.0"),
      TransactionType.INCOME,
      new TransactionCategoryResponse(1L, "Category1", "Category1 description")
    );

    final var mockUser = mockUsers.get(0);
    Authentication authentication = getAuthenticationForUser(mockUser);

    when(transactionRepository.findByIdAndUserId(id, mockUser.getId())).thenReturn(transaction);
    when(transactionMapper.toResponse(transaction.orElseThrow())).thenReturn(expectedTransactionResponse);

    // when
    TransactionResponse transactionResponse = transactionsService.getTransactionByUser(id, authentication);

    // then
    assertEquals(expectedTransactionResponse, transactionResponse);
  }

  @Test
  public void testAddTransaction() {
    // given
    final var user = new User(1L, "User 1", "Name 1", "email 1", "pass 1", Role.USER);
    final var category = new TransactionCategory(
      1L,
      user,
      "Category1",
      "Category1 description",
      Set.of()
    );
    final var request = new TransactionRequest(
      new Date(),
      "Transaction1",
      new BigDecimal("100.0"),
      TransactionType.INCOME,
      category.getId()
    );
    final var expectedTransactionResponse = new TransactionResponse(
      1L,
      new Date(),
      "Transaction1",
      new BigDecimal("100.0"),
      TransactionType.INCOME,
      new TransactionCategoryResponse(1L, "Category1", "Category1 description")
    );
    final var newTransaction = Transaction.builder()
      .date(request.getDate())
      .description(request.getDescription())
      .amount(request.getAmount())
      .category(
        mockTransactionCategories.stream()
          .filter(c -> c.getId().equals(request.getCategoryId()))
          .findFirst()
          .orElseThrow()
      )
      .build();

    final var mockUser = mockUsers.get(0);
    Authentication authentication = getAuthenticationForUser(mockUser);

    when(transactionMapper.toEntity(request, category, user)).thenReturn(newTransaction);
    when(transactionMapper.toResponse(newTransaction)).thenReturn(expectedTransactionResponse);
    when(transactionCategoryRepository.findByIdAndUserId(request.getCategoryId(), mockUser.getId())).thenReturn(Optional.of(category));
    when(transactionRepository.save(newTransaction)).thenReturn(newTransaction);

    // when
    final TransactionResponse transactionResponse = transactionsService.addTransactionForUser(request, authentication);

    // then
    assertEquals(expectedTransactionResponse, transactionResponse);
  }

}
