package com.tmilkov.moneymate.mapper;

import com.tmilkov.moneymate.model.response.AuthenticationResponse;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationMapper {
    public AuthenticationResponse toResponse(String token) {
        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }
}