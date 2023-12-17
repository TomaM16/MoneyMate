package com.tmilkov.moneymate.service.authentication;

import com.tmilkov.moneymate.mapper.AuthenticationMapper;
import com.tmilkov.moneymate.model.entity.user.Role;
import com.tmilkov.moneymate.model.entity.user.User;
import com.tmilkov.moneymate.model.request.AuthenticationRequest;
import com.tmilkov.moneymate.model.request.RegisterRequest;
import com.tmilkov.moneymate.model.response.AuthenticationResponse;
import com.tmilkov.moneymate.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  private final AuthenticationMapper authenticationMapper;

  public AuthenticationResponse register(RegisterRequest request) {
    var user = User.builder()
      .firstname(request.getFirstName())
      .lastname(request.getLastName())
      .email(request.getEmail())
      .password(passwordEncoder.encode(request.getPassword()))
      .role(Role.USER)
      .build();

    userRepository.save(user);

    var jwtToken = jwtService.generateToken(user);

    return authenticationMapper.toResponse(jwtToken);
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(
        request.getEmail(),
        request.getPassword()
      )
    );

    var user = userRepository.findByEmail(request.getEmail())
      .orElseThrow();

    var jwtToken = jwtService.generateToken(user);

    return authenticationMapper.toResponse(jwtToken);
  }
}
