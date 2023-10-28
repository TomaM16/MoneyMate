package com.tmilkov.moneymate.restcontroller.budget;

import com.tmilkov.moneymate.model.request.BudgetPlanRequest;
import com.tmilkov.moneymate.model.response.BudgetPlanResponse;
import com.tmilkov.moneymate.service.budget.BudgetPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/budget-plans")
@RequiredArgsConstructor
public class BudgetPlanRestController {

    private final BudgetPlanService service;

    // Get all budget plans by category
    @GetMapping("/{categoryId}")
    public ResponseEntity<List<BudgetPlanResponse>> getAllBudgetPlansByCategory(
            @PathVariable Long categoryId
    ) {
        return ResponseEntity.ok(service.getAllBudgetPlansByCategory(categoryId));
    }

    // Add budget plan
    @PostMapping
    public ResponseEntity<BudgetPlanResponse> addBudgetPlan(
            @RequestBody @Valid BudgetPlanRequest request
    ) {
        return ResponseEntity.ok(service.addBudgetPlan(request));
    }

}
