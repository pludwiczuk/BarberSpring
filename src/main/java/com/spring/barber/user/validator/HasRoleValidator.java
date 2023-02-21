package com.spring.barber.user.validator;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.spring.barber.user.Role;
import com.spring.barber.user.User;
import com.spring.barber.user.UserRepository;

public class HasRoleValidator implements ConstraintValidator<HasRole, Long> {

  private Role role;

  @Autowired
  private UserRepository userRepository;

  @Override
  public void initialize(HasRole constraintAnnotation) {
    this.role = constraintAnnotation.role();
  }

  @Override
  public boolean isValid(Long id, ConstraintValidatorContext constraintValidatorContext) {
    Optional<User> userOptional = userRepository.findById(id);
    if (!userOptional.isPresent()) {
      throw new RuntimeException("User not found");
    }
    User user = userOptional.get();
    return user.getRole().equals(this.role);
  }

}