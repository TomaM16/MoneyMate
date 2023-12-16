package com.tmilkov.moneymate.restcontroller.budget;

import com.tmilkov.moneymate.model.request.BudgetPlanRequest;
import com.tmilkov.moneymate.model.response.BudgetPlanResponse;
import com.tmilkov.moneymate.model.response.BudgetResponse;
import com.tmilkov.moneymate.service.budget.BudgetPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/budget")
@RequiredArgsConstructor
public class BudgetPlanRestController {

    private final BudgetPlanService service;

    @GetMapping
    public ResponseEntity<BudgetResponse> getBudget() {
        return ResponseEntity.ok(service.getBudget());
    }

    @GetMapping("/plans")
    public ResponseEntity<List<BudgetPlanResponse>> getAllBudgetPlans() {
        return ResponseEntity.ok(service.getAllBudgetPlans());
    }

    @PostMapping("/plans")
    public ResponseEntity<BudgetPlanResponse> addBudgetPlan(
            @RequestBody @Valid BudgetPlanRequest request
    ) {
        return ResponseEntity.ok(service.addBudgetPlan(request));
    }

}
