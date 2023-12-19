package com.tmilkov.moneymate.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class BudgetResponse {
  private final int used;
  private final BigDecimal balance;
  private final BigDecimal income;
  private final BigDecimal expenses;
}
