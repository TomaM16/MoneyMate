package com.tmilkov.moneymate.service.category;

import com.tmilkov.moneymate.mapper.TransactionCategoryMapper;
import com.tmilkov.moneymate.model.entity.transaction.TransactionCategory;
import com.tmilkov.moneymate.model.request.TransactionCategoryRequest;
import com.tmilkov.moneymate.model.response.TransactionCategoryResponse;
import com.tmilkov.moneymate.repository.transaction.TransactionCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

  private final TransactionCategoryRepository transactionCategoryRepository;
  private final TransactionCategoryMapper transactionCategoryMapper;

  public List<TransactionCategoryResponse> getAllCategories() {
    return transactionCategoryRepository.findAll()
      .stream()
      .map(transactionCategoryMapper::toResponse)
      .toList();
  }

  public TransactionCategoryResponse addCategory(TransactionCategoryRequest request) {
    final TransactionCategory category = transactionCategoryMapper.toEntity(request);
    final var transactionCategory = transactionCategoryRepository.save(category);

    return transactionCategoryMapper.toResponse(transactionCategory);
  }

}
