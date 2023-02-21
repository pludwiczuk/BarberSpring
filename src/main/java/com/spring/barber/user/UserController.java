package com.spring.barber.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

import javax.validation.Valid;

import com.spring.barber.auth.AuthService;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @PostMapping("/users")
    ResponseEntity<?> create(@RequestBody @Valid CreateUserDto dto) {
        try {
            User user = userService.create(dto);
            return new ResponseEntity<>(new UserResponse(user), HttpStatus.CREATED);
        } catch (UserAlreadyExistException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

    @GetMapping("/users/me")
    ResponseEntity<?> getCurrentUser() {
        UserResponse user = userService.getCurrentUser();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PatchMapping("/users/me")
    ResponseEntity<?> updateCurrentUser(@RequestBody Map<String, String> fields) {
        if (fields.containsKey("role")) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "'role' field is forbidden");
        }
        try {
            User user = userService.updateCurrentUser(fields);
            return new ResponseEntity<>(new UserResponse(user), HttpStatus.OK);
        } catch (UserAlreadyExistException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

}
