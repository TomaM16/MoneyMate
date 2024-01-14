package com.tmilkov.moneymate.config;

import com.tmilkov.moneymate.model.entity.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

  private static final String[] WHITE_LIST_URL = {
    "/css/**",
    "/js/**",
    "/images/**",
    "/api/v1/auth/**",
//    "/api/v1/**"  // TODO: Remove when authentication is added
  };
  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final AuthenticationProvider authenticationProvider;
  private final LogoutHandler logoutHandler;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .csrf(AbstractHttpConfigurer::disable)
      .authorizeHttpRequests(auth -> auth
        .requestMatchers(WHITE_LIST_URL).permitAll()
        .requestMatchers(HttpMethod.OPTIONS, "/api/**").permitAll()
        .requestMatchers("/api/v1/demo-controller").hasAnyRole(Role.ADMIN.name())
        .anyRequest().authenticated()
      )
      .sessionManagement(sessionManagement ->
        sessionManagement
          .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      )
      .authenticationProvider(authenticationProvider)
      .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
      .logout(logout ->
        logout.logoutUrl("/api/v1/auth/logout")
          .addLogoutHandler(logoutHandler)
          .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
      );

    return http.build();
  }
}
