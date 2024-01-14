package com.tmilkov.moneymate.config;

import com.tmilkov.moneymate.mapper.AuthenticationMapper;
import com.tmilkov.moneymate.mapper.UserMapper;
import com.tmilkov.moneymate.model.entity.user.Role;
import com.tmilkov.moneymate.model.entity.user.User;
import com.tmilkov.moneymate.model.request.RegisterRequest;
import com.tmilkov.moneymate.repository.user.UserRepository;
import com.tmilkov.moneymate.service.authentication.AuthenticationService;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminUserInitializer implements CommandLineRunner {

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
  public void run(String... args) throws Exception {
    createAdminUserIfNotExists();
  }

  private void createAdminUserIfNotExists() {
    if (userRepository.findByEmail(Constants.ADMIN_EMAIL).isEmpty()) {
      RegisterRequest adminUserRequest = RegisterRequest.builder()
        .firstName(Constants.ADMIN_FIRSTNAME)
        .lastName(Constants.ADMIN_LASTNAME)
        .email(Constants.ADMIN_EMAIL)
        .password(Constants.ADMIN_PASSWORD)
        .role(Role.ADMIN)
        .build();

      System.out.println("Admin token: " + authenticationService.register(adminUserRequest).getAccessToken());
    }
  }

  public interface Constants {
    String ADMIN_FIRSTNAME = "Admin";
    String ADMIN_LASTNAME = "User";
    String ADMIN_EMAIL = "admin@moneymate.com";
    String ADMIN_PASSWORD = "admin-tmilkov";
  }

}
