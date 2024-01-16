package com.tmilkov.moneymate.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionCategoryRequest {

  @NotBlank(message = "Name is required")
  private String name;

  private String description;
}
