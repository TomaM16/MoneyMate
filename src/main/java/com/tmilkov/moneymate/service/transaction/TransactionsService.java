package com.tmilkov.moneymate.service.transaction;

import com.tmilkov.moneymate.mapper.TransactionMapper;
import com.tmilkov.moneymate.model.entity.transaction.Transaction;
import com.tmilkov.moneymate.model.entity.transaction.TransactionCategory;
import com.tmilkov.moneymate.model.entity.user.User;
import com.tmilkov.moneymate.model.request.TransactionRequest;
import com.tmilkov.moneymate.model.response.TransactionResponse;
import com.tmilkov.moneymate.repository.transaction.TransactionCategoryRepository;
import com.tmilkov.moneymate.repository.transaction.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TransactionsService {

  private final TransactionRepository transactionRepository;
  private final TransactionCategoryRepository transactionCategoryRepository;
  private final TransactionMapper transactionMapper;

  public List<TransactionResponse> getAllTransactionsByUser(Principal connectedUser) {
    final var userId = ((User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal()).getId();

    return transactionRepository.findAllByUserId(userId).stream()
      .map(transactionMapper::toResponse)
      .toList();
  }

  public List<TransactionResponse> getRecentTransactionsByUser(Principal connectedUser) {
    final var userId = ((User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal()).getId();

    return transactionRepository.findAllByUserId(userId).stream()
      .sorted(Collections.reverseOrder())
      .map(transactionMapper::toResponse)
      .limit(Constants.NUMBER_RECENT_TRANSACTIONS)
      .toList();
  }

  public TransactionResponse getTransactionByUser(Long id, Principal connectedUser) throws NoSuchElementException {
    final var userId = ((User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal()).getId();

    final var transaction = transactionRepository.findByIdAndUserId(id, userId).orElseThrow();
    return transactionMapper.toResponse(transaction);
  }

  private TransactionCategory getTransactionCategoryByUserId(Long id, Long userId) throws NoSuchElementException {
    return transactionCategoryRepository.findByIdAndUserId(id, userId).orElseThrow();
  }

  public TransactionResponse addTransactionForUser(TransactionRequest request, Principal connectedUser) throws NoSuchElementException {
    final var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

    final TransactionCategory category = getTransactionCategoryByUserId(request.getCategoryId(), user.getId());
    final Transaction newTransaction = transactionMapper.toEntity(request, category, user);
    final Transaction transaction = transactionRepository.save(newTransaction);

    return transactionMapper.toResponse(transaction);
  }

  public Void deleteTransactionForUser(Long transactionId, Principal connectedUser) {
    final var userId = ((User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal()).getId();

    transactionRepository.deleteByIdAndUserId(transactionId, userId);
    return null;
  }

  public interface Constants {
    int NUMBER_RECENT_TRANSACTIONS = 5;
  }

}
