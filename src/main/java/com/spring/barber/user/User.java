package com.spring.barber.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String firstName;
    
    @NotNull
    private String lastName;

    @NotNull
    @Column(unique = true)
    private String username;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    private String password;
    
    @NotNull
    private Role role;

    public User(CreateUserDto dto) {
        this.firstName = dto.getFirstName();
        this.lastName = dto.getLastName();
        this.username = dto.getUsername();
        this.email = dto.getEmail();
        this.password = null;
        this.role = convertRole(dto.getRole());
    }

    private Role convertRole(String role) {
        switch (role) {
            case "user":
                return Role.ROLE_USER;
            case "businessUser":
                return Role.ROLE_BUSINESS_USER;
            default:
                throw new RuntimeException("Unknown role");
        }
    }
    
}
