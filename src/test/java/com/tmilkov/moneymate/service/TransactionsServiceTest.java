package com.tmilkov.moneymate.service;

import com.tmilkov.moneymate.model.entity.transaction.Transaction;
import com.tmilkov.moneymate.model.entity.transaction.TransactionCategory;
import com.tmilkov.moneymate.model.entity.transaction.TransactionType;
import com.tmilkov.moneymate.model.request.TransactionCategoryRequest;
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

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class TransactionsServiceTest {

    @Mock
    private TransactionCategoryRepository transactionCategoryRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @InjectMocks
    private TransactionsService transactionsService;

    private static List<TransactionCategory> mockTransactionCategories;
    private static List<Transaction> mockTransactions;

    @BeforeAll
    static void before() {
        mockTransactionCategories = List.of(
                new TransactionCategory(1L, "Category1", "Category1 description"),
                new TransactionCategory(2L, "Category2", "Category2 description"),
                new TransactionCategory(3L, "Category3", "Category3 description")
        );
        mockTransactions = List.of(
                new Transaction(
                        1L,
                        new Date(),
                        "Transaction1",
                        new BigDecimal("100.0"),
                        TransactionType.INCOME,
                        new TransactionCategory(1L, "Category1", "Category1 description")
                ),
                new Transaction(
                        2L,
                        new Date(),
                        "Transaction2",
                        new BigDecimal("100.0"),
                        TransactionType.INCOME,
                        new TransactionCategory(2L, "Category1", "Category1 description")
                ),
                new Transaction(
                        3L,
                        new Date(),
                        "Transaction3",
                        new BigDecimal("100.0"),
                        TransactionType.INCOME,
                        new TransactionCategory(3L, "Category1", "Category1 description")
                )
        );
    }

    @Test
    public void testGetAllCategories() {
        // given
        when(transactionCategoryRepository.findAll()).thenReturn(mockTransactionCategories);

        // when
        List<TransactionCategoryResponse> categories = transactionsService.getAllCategories();

        // then
        IntStream.range(0, mockTransactionCategories.size())
                .forEach(i -> {
                    assertEquals(mockTransactionCategories.get(i).getId(), categories.get(i).getId());
                    assertEquals(mockTransactionCategories.get(i).getName(), categories.get(i).getName());
                    assertEquals(mockTransactionCategories.get(i).getDescription(), categories.get(i).getDescription());
                });
    }

    @Test
    public void testGetAllTransactions() {
        // given
        when(transactionRepository.findAll()).thenReturn(mockTransactions);

        // when
        List<TransactionResponse> transactions = transactionsService.getAllTransactions();

        // then
        IntStream.range(0, mockTransactions.size())
                .forEach(i -> {
                    assertEquals(mockTransactions.get(i).getDate(), transactions.get(i).getDate());
                    assertEquals(mockTransactions.get(i).getDescription(), transactions.get(i).getDescription());
                    assertEquals(mockTransactions.get(i).getAmount(), transactions.get(i).getAmount());
                    assertEquals(mockTransactions.get(i).getType(), transactions.get(i).getType());
                    assertEquals(mockTransactions.get(i).getCategory(), transactions.get(i).getCategory());
                });
    }

    @Test
    public void testGetTransaction() {
        // given
        final Long id = 1L;
        final var transaction = mockTransactions.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst();

        when(transactionRepository.findById(id)).thenReturn(transaction);

        // when
        TransactionResponse transactionResponse = transactionsService.getTransaction(id);

        // then
        assertEquals(transaction.orElseThrow().getDate(), transactionResponse.getDate());
        assertEquals(transaction.orElseThrow().getDescription(), transactionResponse.getDescription());
        assertEquals(transaction.orElseThrow().getAmount(), transactionResponse.getAmount());
        assertEquals(transaction.orElseThrow().getCategory(), transactionResponse.getCategory());
    }

    @Test
    public void testAddTransaction() {
        // given
        final var request = new TransactionRequest(
                new Date(),
                "Transaction1",
                new BigDecimal("100.0"),
                1L
        );

        final Transaction newTransaction = Transaction.builder()
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

        when(transactionCategoryRepository.findById(request.getCategoryId())).thenReturn(
                mockTransactionCategories.stream()
                        .filter(c -> c.getId().equals(request.getCategoryId()))
                        .findFirst()
        );
        when(transactionRepository.save(newTransaction)).thenReturn(newTransaction);

        // when
        final TransactionResponse transactionResponse = transactionsService.addTransaction(request);

        // then
        assertEquals(request.getDate(), transactionResponse.getDate());
        assertEquals(request.getDescription(), transactionResponse.getDescription());
        assertEquals(request.getAmount(), transactionResponse.getAmount());
        assertEquals(request.getCategoryId(), transactionResponse.getCategory().getId());
    }

    @Test
    public void testAddCategory() {
        // given
        final TransactionCategoryRequest request = new TransactionCategoryRequest(
                "Category1",
                "Category1 description"
        );
        final TransactionCategory transactionCategory = TransactionCategory.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        when(transactionCategoryRepository.save(transactionCategory)).thenReturn(transactionCategory);

        // when
        final TransactionCategoryResponse transactionCategoryResponse = transactionsService.addCategory(request);

        // then
        assertEquals(request.getName(), transactionCategoryResponse.getName());
        assertEquals(request.getDescription(), transactionCategoryResponse.getDescription());
    }

}