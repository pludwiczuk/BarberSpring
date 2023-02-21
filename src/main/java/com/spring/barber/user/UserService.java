package com.spring.barber.user;

import java.lang.reflect.Field;
import java.util.Map;

import com.spring.barber.auth.AuthService;
import com.spring.barber.business.BusinessResponse;
import com.spring.barber.businessUser.BusinessUser;
import com.spring.barber.businessUser.BusinessUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthService authService;
    
    @Autowired
    private BusinessUserService businessUserService;

    public User create(CreateUserDto dto) throws UserAlreadyExistException {
        this.checkEmailConstraintViolation(dto.getEmail());
        this.checkUsernameConstraintViolation(dto.getUsername());
        User user = new User(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        return userRepository.save(user);
    }

    public User updateCurrentUser(Map<String, String> fields) throws UserAlreadyExistException {
        User user = authService.getAuthenticatedUser();
        fields.forEach((key, value) -> {
            try {
                Field field = ReflectionUtils.findField(User.class, key);
                field.setAccessible(true);
                ReflectionUtils.setField(field, user, value);
            } catch (Exception e) {
                // ignore non existing fields in user
            }
        });
        if (fields.containsKey("email")) this.checkEmailConstraintViolation(user.getEmail());
        if (fields.containsKey("username")) this.checkUsernameConstraintViolation(user.getUsername());
        if (fields.containsKey("password")) user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public UserResponse getCurrentUser() {
        User user = authService.getAuthenticatedUser();
        UserResponse userResponse = new UserResponse(user);
        if (user.getRole() == Role.ROLE_BUSINESS_USER) {
            BusinessUser bu = businessUserService.findOneByUserId(user.getId());
            if (bu != null) {
                userResponse.setBusiness(new BusinessResponse(bu.getBusiness()));
            }
        }
        return userResponse;
    }

    private void checkEmailConstraintViolation(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistException("Provided email is already taken");
        }
    }

    private void checkUsernameConstraintViolation(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistException("Provided username is already taken");
        }
    }
    
}
