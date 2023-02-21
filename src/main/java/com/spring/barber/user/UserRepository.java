package com.spring.barber.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
    User findByUsername(String username);
    boolean existsById(Long id);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

}
