package com.spring.barber.auth;

import com.spring.barber.businessUser.BusinessUserRepository;
import com.spring.barber.user.User;
import com.spring.barber.user.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private BusinessUserRepository businessUserRepository;

  public User getAuthenticatedUser() {
    SecurityContext sc = SecurityContextHolder.getContext();
    String currentlyAuthenticatedUsername = sc.getAuthentication().getName();
    User user = userRepository.findByUsername(currentlyAuthenticatedUsername);
    return user;
  }

  public boolean hasAccessToBusiness(long businessId) {
    User authenticatedUser = getAuthenticatedUser();
    return businessUserRepository.existsByUserIdAndBusinessId(authenticatedUser.getId(), businessId);
  }

}
