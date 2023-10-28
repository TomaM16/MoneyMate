package com.tmilkov.moneymate.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
public class BudgetPlanResponse {
    private final Long id;
    private final String name;
    private final Date startDate;
    private final Date endDate;
    private final BigDecimal monthlyBudget;
}
