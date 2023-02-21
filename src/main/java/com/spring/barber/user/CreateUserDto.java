package com.spring.barber.user;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class CreateUserDto {

    @NotEmpty(message = "'firstName' must not be empty")
    private String firstName;

    @NotEmpty(message = "'lastName' must not be empty")
    private String lastName;

    @NotEmpty(message = "'username' must not be empty")
    private String username;

    @Email(message = "'email' must be a valid email")
    @NotEmpty(message = "'email' must not be empty")
    private String email;

    @NotEmpty(message = "'password' must not be empty")
    private String password;

    @Pattern(regexp = "(user|businessUser)$", message = "'role' must be one of: [user, businessUser]")
    @NotEmpty(message = "'role' must not be empty")
    private String role; 
    
}
