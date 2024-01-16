package com.tmilkov.moneymate.restcontroller.budget;

import com.tmilkov.moneymate.model.request.BudgetPlanRequest;
import com.tmilkov.moneymate.model.response.BudgetPlanResponse;
import com.tmilkov.moneymate.model.response.BudgetResponse;
import com.tmilkov.moneymate.service.budget.BudgetPlanService;
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
@RequestMapping("/api/v1/budget")
@RequiredArgsConstructor
public class BudgetPlanRestController {

  private final BudgetPlanService service;

  @GetMapping
  public ResponseEntity<BudgetResponse> getBudget(Principal connectedUser) {
    return ResponseEntity.ok(service.getBudgetByUser(connectedUser));
  }

  @GetMapping("/plans")
  public ResponseEntity<List<BudgetPlanResponse>> getAllBudgetPlans(Principal connectedUser) {
    return ResponseEntity.ok(service.getAllBudgetPlansByUser(connectedUser));
  }

  @PostMapping("/plans")
  public ResponseEntity<BudgetPlanResponse> addBudgetPlan(
    @RequestBody @Valid BudgetPlanRequest request,
    Principal connectedUser
  ) {
    return ResponseEntity.ok(service.addBudgetPlanForUser(request, connectedUser));
  }

}
