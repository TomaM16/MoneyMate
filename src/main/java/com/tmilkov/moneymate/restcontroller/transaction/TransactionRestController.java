package com.tmilkov.moneymate.restcontroller.transaction;

import com.tmilkov.moneymate.model.request.TransactionRequest;
import com.tmilkov.moneymate.model.response.TransactionResponse;
import com.tmilkov.moneymate.service.transaction.TransactionsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionRestController {

  private final TransactionsService service;

  // Get a specific transaction by ID
  @GetMapping("/{transactionId}")
  public ResponseEntity<TransactionResponse> getTransaction(
    @PathVariable Long transactionId,
    Principal connectedUser
  ) {
    return ResponseEntity.ok(service.getTransactionByUser(transactionId, connectedUser));
  }

  // Get all transactions
  @GetMapping
  public ResponseEntity<List<TransactionResponse>> getAllTransactions(Principal connectedUser) {
    return ResponseEntity.ok(service.getAllTransactionsByUser(connectedUser));
  }

  // Add a new transaction
  @PostMapping
  public ResponseEntity<TransactionResponse> addTransaction(
    @RequestBody @Valid TransactionRequest request,
    Principal connectedUser
  ) {
    return ResponseEntity.ok(service.addTransactionForUser(request, connectedUser));
  }

  // Delete a specific transaction by ID
  @DeleteMapping("/{transactionId}")
  public ResponseEntity<Void> deleteTransaction(@PathVariable Long transactionId, Principal connectedUser) {
    return ResponseEntity.ok(service.deleteTransactionForUser(transactionId, connectedUser));
  }

  @GetMapping("/recent")
  public ResponseEntity<List<TransactionResponse>> getRecentTransactions(Principal connectedUser) {
    return ResponseEntity.ok(service.getRecentTransactionsByUser(connectedUser));
  }

}
