package com.spring.barber.location;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {

    boolean existsById(Long id);
    List<Location> findAllByBusinessId(Long businessId);
    List<Location> findAllByAddressCityContaining(String city);

}
