package com.tmilkov.moneymate.service.transaction;

import com.tmilkov.moneymate.model.entity.transaction.Transaction;
import com.tmilkov.moneymate.model.entity.transaction.TransactionCategory;
import com.tmilkov.moneymate.model.request.TransactionCategoryRequest;
import com.tmilkov.moneymate.model.request.TransactionRequest;
import com.tmilkov.moneymate.model.response.TransactionCategoryResponse;
import com.tmilkov.moneymate.model.response.TransactionResponse;
import com.tmilkov.moneymate.repository.transaction.TransactionCategoryRepository;
import com.tmilkov.moneymate.repository.transaction.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TransactionsService {

    private final TransactionRepository transactionRepository;
    private final TransactionCategoryRepository transactionCategoryRepository;

    public List<TransactionCategoryResponse> getAllCategories() {
        return transactionCategoryRepository.findAll()
                .stream()
                .map(transactionCategory -> new TransactionCategoryResponse(
                        transactionCategory.getName(),
                        transactionCategory.getDescription()
                ))
                .toList();
    }

    public List<TransactionResponse> getAllTransactions() {
        return transactionRepository.findAll()
                .stream()
                .map(transaction -> new TransactionResponse(
                        transaction.getDate(),
                        transaction.getDescription(),
                        transaction.getAmount(),
                        transaction.getCategory()
                ))
                .toList();
    }

    public TransactionResponse getTransaction(Long id) throws NoSuchElementException {
        final var transaction = transactionRepository.findById(id).orElseThrow();

        return new TransactionResponse(
                transaction.getDate(),
                transaction.getDescription(),
                transaction.getAmount(),
                transaction.getCategory()
        );
    }

    private TransactionCategory getTransactionCategory(Long id) throws NoSuchElementException {
        return transactionCategoryRepository.findById(id).orElseThrow();
    }

    public TransactionResponse addTransaction(TransactionRequest request) throws NoSuchElementException {
        Transaction newTransaction = Transaction.builder()
                .date(request.getDate())
                .description(request.getDescription())
                .amount(request.getAmount())
                .category(getTransactionCategory(request.getCategoryId()))
                .build();

        final Transaction transaction = transactionRepository.save(newTransaction);

        return new TransactionResponse(
                transaction.getDate(),
                transaction.getDescription(),
                transaction.getAmount(),
                transaction.getCategory()
        );
    }

    public Void deleteTransaction(Long transactionId) {
        transactionRepository.deleteById(transactionId);
        return null;
    }

    public TransactionCategoryResponse addCategory(TransactionCategoryRequest request) {
        final var transactionCategory = transactionCategoryRepository.save(
                TransactionCategory.builder()
                        .name(request.getName())
                        .description(request.getDescription())
                        .build()
        );

        return new TransactionCategoryResponse(
                transactionCategory.getName(),
                transactionCategory.getDescription()
        );
    }
}
