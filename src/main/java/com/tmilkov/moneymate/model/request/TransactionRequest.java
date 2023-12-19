package com.tmilkov.moneymate.model.request;

import com.tmilkov.moneymate.model.entity.transaction.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {

  @NotNull(message = "Date is required")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private Date date;

  @NotBlank(message = "Description is required")
  private String description;

  @NotNull(message = "Amount is required")
  private BigDecimal amount;

  @NotNull(message = "Type is required")
  private TransactionType type;

  @NotNull(message = "Category is required")
  private Long categoryId;

}
