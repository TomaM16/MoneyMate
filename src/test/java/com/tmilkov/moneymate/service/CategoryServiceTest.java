package com.tmilkov.moneymate.service;

import com.tmilkov.moneymate.mapper.TransactionCategoryMapper;
import com.tmilkov.moneymate.model.entity.transaction.TransactionCategory;
import com.tmilkov.moneymate.model.entity.user.Role;
import com.tmilkov.moneymate.model.entity.user.User;
import com.tmilkov.moneymate.model.request.TransactionCategoryRequest;
import com.tmilkov.moneymate.model.response.TransactionCategoryResponse;
import com.tmilkov.moneymate.repository.transaction.TransactionCategoryRepository;
import com.tmilkov.moneymate.service.category.CategoryService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

  @Mock
  private TransactionCategoryRepository transactionCategoryRepository;
  @Mock
  private TransactionCategoryMapper transactionCategoryMapper;

  @InjectMocks
  private CategoryService categoryService;

  private static List<User> mockUsers;
  private static List<TransactionCategory> mockTransactionCategories;

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
  }

  private Authentication getAuthenticationForUser(User user) {
    return new UsernamePasswordAuthenticationToken(
      user,
      null,
      user.getAuthorities()
    );
  }

  @Test
  public void testGetAllCategories() {
    // given
    final var expectedCategoriesResponses = List.of(
      new TransactionCategoryResponse(1L, "Category1", "Category1 description"),
      new TransactionCategoryResponse(2L, "Category2", "Category2 description"),
      new TransactionCategoryResponse(3L, "Category3", "Category3 description")
    );

    final var mockUser = mockUsers.get(0);
    final var authentication = getAuthenticationForUser(mockUser);

    when(transactionCategoryMapper.toResponse(any(TransactionCategory.class)))
      .thenAnswer(invocation -> {
        TransactionCategory inputTransactionCategory = invocation.getArgument(0);
        return expectedCategoriesResponses.stream()
          .filter(response -> response.getId().equals(inputTransactionCategory.getId()))
          .findFirst()
          .orElse(null);
      });
    when(transactionCategoryRepository.findAllByUserId(mockUser.getId())).thenReturn(mockTransactionCategories);

    // when
    List<TransactionCategoryResponse> categories = categoryService.getAllCategoriesByUser(authentication);

    // then
    assertEquals(expectedCategoriesResponses, categories);
  }

  @Test
  public void testAddCategory() {
    // given
    final var request = new TransactionCategoryRequest(
      "Category1",
      "Category1 description"
    );
    final var expectedTransactionCategoryResponse = new TransactionCategoryResponse(
      1L,
      "Category1",
      "Category1 description"
    );
    final var transactionCategory = TransactionCategory.builder()
      .name(request.getName())
      .description(request.getDescription())
      .build();

    final var mockUser = mockUsers.get(0);
    final var authentication = getAuthenticationForUser(mockUser);

    when(transactionCategoryMapper.toEntity(request, mockUser)).thenReturn(transactionCategory);
    when(transactionCategoryMapper.toResponse(transactionCategory)).thenReturn(expectedTransactionCategoryResponse);
    when(transactionCategoryRepository.save(transactionCategory)).thenReturn(transactionCategory);

    // when
    final TransactionCategoryResponse transactionCategoryResponse = categoryService.addCategoryForUser(request, authentication);

    // then
    assertEquals(expectedTransactionCategoryResponse, transactionCategoryResponse);
  }
}
