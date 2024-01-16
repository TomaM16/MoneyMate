package com.tmilkov.moneymate.mapper;

import com.tmilkov.moneymate.model.entity.user.User;
import com.tmilkov.moneymate.model.request.RegisterRequest;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
  public User toEntity(RegisterRequest request, String password) {
    return User.builder()
      .firstname(request.getFirstName())
      .lastname(request.getLastName())
      .email(request.getEmail())
      .password(password)
      .role(request.getRole())
      .build();
  }
}
