package com.tmilkov.moneymate.service.category;

import com.tmilkov.moneymate.mapper.TransactionCategoryMapper;
import com.tmilkov.moneymate.model.entity.transaction.TransactionCategory;
import com.tmilkov.moneymate.model.entity.user.User;
import com.tmilkov.moneymate.model.request.TransactionCategoryRequest;
import com.tmilkov.moneymate.model.response.TransactionCategoryResponse;
import com.tmilkov.moneymate.repository.transaction.TransactionCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

  private final TransactionCategoryRepository transactionCategoryRepository;
  private final TransactionCategoryMapper transactionCategoryMapper;

  public List<TransactionCategoryResponse> getAllCategoriesByUser(Principal connectedUser) {
    final var userId = ((User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal()).getId();

    return transactionCategoryRepository.findAllByUserId(userId)
      .stream()
      .map(transactionCategoryMapper::toResponse)
      .toList();
  }

  public TransactionCategoryResponse addCategoryForUser(
    TransactionCategoryRequest request,
    Principal connectedUser
  ) {
    final var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

    final TransactionCategory category = transactionCategoryMapper.toEntity(request, user);
    final var transactionCategory = transactionCategoryRepository.save(category);

    return transactionCategoryMapper.toResponse(transactionCategory);
  }

}
