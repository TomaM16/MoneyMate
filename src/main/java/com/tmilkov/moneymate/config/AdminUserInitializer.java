package com.tmilkov.moneymate.config;

import com.tmilkov.moneymate.model.entity.user.Role;
import com.tmilkov.moneymate.model.request.RegisterRequest;
import com.tmilkov.moneymate.repository.user.UserRepository;
import com.tmilkov.moneymate.service.authentication.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminUserInitializer implements CommandLineRunner {

  @Value("${moneymate.admin.email}")
  private String adminEmail;
  @Value("${moneymate.admin.password}")
  private String adminPassword;

  private final UserRepository userRepository;
  private final AuthenticationService authenticationService;

  @Autowired
  public AdminUserInitializer(
    UserRepository userRepository,
    AuthenticationService authenticationService
  ) {
    this.userRepository = userRepository;
    this.authenticationService = authenticationService;
  }

  @Override
  public void run(String... args) {
    createAdminUserIfNotExists();
  }

  private void createAdminUserIfNotExists() {
    if (userRepository.findByEmail(adminEmail).isEmpty()) {
      RegisterRequest adminUserRequest = RegisterRequest.builder()
        .firstName(Constants.ADMIN_FIRSTNAME)
        .lastName(Constants.ADMIN_LASTNAME)
        .email(adminEmail)
        .password(adminPassword)
        .role(Role.ADMIN)
        .build();

      System.out.println("Admin token: " + authenticationService.register(adminUserRequest).getAccessToken());
    }
  }

  public interface Constants {
    String ADMIN_FIRSTNAME = "Admin";
    String ADMIN_LASTNAME = "User";
  }

}
