package com.tmilkov.moneymate.mapper;

import com.tmilkov.moneymate.model.response.AuthenticationResponse;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationMapper {
  public AuthenticationResponse toResponse(String jwtToken, String refreshToken) {
    return AuthenticationResponse.builder()
      .accessToken(jwtToken)
      .refreshToken(refreshToken)
      .build();
  }
}
