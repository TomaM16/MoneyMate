package com.tmilkov.moneymate.service;

import com.tmilkov.moneymate.mapper.TransactionMapper;
import com.tmilkov.moneymate.model.entity.transaction.Transaction;
import com.tmilkov.moneymate.model.entity.transaction.TransactionCategory;
import com.tmilkov.moneymate.model.entity.transaction.TransactionType;
import com.tmilkov.moneymate.model.request.TransactionRequest;
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

    private static List<TransactionCategory> mockTransactionCategories;
    private static List<Transaction> mockTransactions;

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
                        new BigDecimal("100.0"),
                        TransactionType.INCOME,
                        new TransactionCategory(1L, "Category1", "Category1 description", Set.of())
                ),
                new Transaction(
                        2L,
                        new Date(),
                        "Transaction2",
                        new BigDecimal("100.0"),
                        TransactionType.INCOME,
                        new TransactionCategory(2L, "Category1", "Category1 description", Set.of())
                ),
                new Transaction(
                        3L,
                        new Date(),
                        "Transaction3",
                        new BigDecimal("100.0"),
                        TransactionType.INCOME,
                        new TransactionCategory(3L, "Category1", "Category1 description", Set.of())
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
                        new TransactionCategory(1L, "Category1", "Category1 description", Set.of())
                ),
                new TransactionResponse(
                        2L,
                        new Date(),
                        "Transaction2",
                        new BigDecimal("100.0"),
                        TransactionType.INCOME,
                        new TransactionCategory(2L, "Category1", "Category1 description", Set.of())
                ),
                new TransactionResponse(
                        3L,
                        new Date(),
                        "Transaction3",
                        new BigDecimal("100.0"),
                        TransactionType.INCOME,
                        new TransactionCategory(3L, "Category1", "Category1 description", Set.of())
                )
        );

        when(transactionMapper.toResponse(any(Transaction.class)))
                .thenAnswer(invocation -> {
                    Transaction inputTransaction = invocation.getArgument(0);
                    return expectedTransactionResponses.stream()
                            .filter(response -> response.getId().equals(inputTransaction.getId()))
                            .findFirst()
                            .orElse(null);
                });
        when(transactionRepository.findAll()).thenReturn(mockTransactions);

        // when
        List<TransactionResponse> transactions = transactionsService.getAllTransactions();

        // then
        assertEquals(expectedTransactionResponses, transactions);
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
                new TransactionCategory(1L, "Category1", "Category1 description", Set.of())
        );
        when(transactionRepository.findById(id)).thenReturn(transaction);
        when(transactionMapper.toResponse(transaction.orElseThrow())).thenReturn(expectedTransactionResponse);

        // when
        TransactionResponse transactionResponse = transactionsService.getTransaction(id);

        // then
        assertEquals(expectedTransactionResponse, transactionResponse);
    }

    @Test
    public void testAddTransaction() {
        // given
        final var category = new TransactionCategory(1L, "Category1", "Category1 description", Set.of());
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
                category
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

        when(transactionMapper.toEntity(request, category)).thenReturn(newTransaction);
        when(transactionMapper.toResponse(newTransaction)).thenReturn(expectedTransactionResponse);
        when(transactionCategoryRepository.findById(request.getCategoryId())).thenReturn(
                mockTransactionCategories.stream()
                        .filter(c -> c.getId().equals(request.getCategoryId()))
                        .findFirst()
        );
        when(transactionRepository.save(newTransaction)).thenReturn(newTransaction);

        // when
        final TransactionResponse transactionResponse = transactionsService.addTransaction(request);

        // then
        assertEquals(expectedTransactionResponse, transactionResponse);
    }

}