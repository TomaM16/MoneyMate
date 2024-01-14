package com.tmilkov.moneymate.restcontroller.category;

import com.tmilkov.moneymate.model.request.TransactionCategoryRequest;
import com.tmilkov.moneymate.model.response.TransactionCategoryResponse;
import com.tmilkov.moneymate.service.category.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryRestController {

  private final CategoryService service;

  // Get all categories
  @GetMapping
  public ResponseEntity<List<TransactionCategoryResponse>> getAllCategories(Principal connectedUser) {
    return ResponseEntity.ok(service.getAllCategoriesByUser(connectedUser));
  }

  // Add a new category
  @PostMapping
  public ResponseEntity<TransactionCategoryResponse> addCategory(
    @RequestBody @Valid TransactionCategoryRequest request,
    Principal connectedUser
  ) {
    return ResponseEntity.ok(service.addCategoryForUser(request, connectedUser));
  }

}
