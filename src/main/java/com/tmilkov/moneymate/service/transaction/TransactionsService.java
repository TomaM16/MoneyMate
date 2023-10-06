package com.tmilkov.moneymate.service.transaction;

import com.tmilkov.moneymate.model.entity.transaction.Transaction;
import com.tmilkov.moneymate.model.request.TransactionRequest;
import com.tmilkov.moneymate.model.response.TransactionResponse;
import com.tmilkov.moneymate.repository.transaction.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TransactionsService {

    private final TransactionRepository transactionRepository;

    public List<TransactionResponse> getAllTransactions() {
        return transactionRepository.findAll().stream().map(transaction -> new TransactionResponse(
                transaction.getDate(),
                transaction.getDescription(),
                transaction.getAmount()
        )).toList();
    }

    public TransactionResponse getTransaction(Long id) throws NoSuchElementException {
        final var transaction = transactionRepository.findById(id).orElseThrow();

        return new TransactionResponse(
                transaction.getDate(),
                transaction.getDescription(),
                transaction.getAmount()
        );
    }

    public TransactionResponse addTransaction(TransactionRequest request) {
        Transaction newTransaction = Transaction.builder()
                .date(request.getDate())
                .description(request.getDescription())
                .amount(request.getAmount())
                .build();

        final Transaction transaction = transactionRepository.save(newTransaction);

        return new TransactionResponse(
                transaction.getDate(),
                transaction.getDescription(),
                transaction.getAmount()
        );
    }

    public Void deleteTransaction(Long transactionId) {
        transactionRepository.deleteById(transactionId);
        return null;
    }

}
