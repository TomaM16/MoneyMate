package com.tmilkov.moneymate.service.transaction;

import com.tmilkov.moneymate.mapper.TransactionCategoryMapper;
import com.tmilkov.moneymate.mapper.TransactionMapper;
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

    private final TransactionMapper transactionMapper;
    private final TransactionCategoryMapper transactionCategoryMapper;

    public List<TransactionCategoryResponse> getAllCategories() {
        return transactionCategoryRepository.findAll()
                .stream()
                .map(transactionCategoryMapper::toResponse)
                .toList();
    }

    public List<TransactionResponse> getAllTransactions() {
        return transactionRepository.findAll()
                .stream()
                .map(transactionMapper::toResponse)
                .toList();
    }

    public TransactionResponse getTransaction(Long id) throws NoSuchElementException {
        final var transaction = transactionRepository.findById(id).orElseThrow();
        return transactionMapper.toResponse(transaction);
    }

    private TransactionCategory getTransactionCategory(Long id) throws NoSuchElementException {
        return transactionCategoryRepository.findById(id).orElseThrow();
    }

    public TransactionResponse addTransaction(TransactionRequest request) throws NoSuchElementException {
        final TransactionCategory category = getTransactionCategory(request.getCategoryId());
        final Transaction newTransaction = transactionMapper.toEntity(request, category);
        final Transaction transaction = transactionRepository.save(newTransaction);

        return transactionMapper.toResponse(transaction);
    }

    public Void deleteTransaction(Long transactionId) {
        transactionRepository.deleteById(transactionId);
        return null;
    }

    public TransactionCategoryResponse addCategory(TransactionCategoryRequest request) {
        final TransactionCategory category = transactionCategoryMapper.toEntity(request);
        final var transactionCategory = transactionCategoryRepository.save(category);

        return transactionCategoryMapper.toResponse(transactionCategory);
    }
}
