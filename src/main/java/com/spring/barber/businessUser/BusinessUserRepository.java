package com.spring.barber.businessUser;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessUserRepository extends JpaRepository<BusinessUser, Long>  {

  boolean existsByUserIdAndBusinessId(Long userId, Long businessId);
  List<BusinessUser> findAllByBusinessId(Long businessId);
  Optional<BusinessUser> findBusinessUserByUserId(Long userId);
  List<BusinessUser> findAllByUserId(Long userId);
  BusinessUser findOneByUserId(Long userId);

}