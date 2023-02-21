package com.spring.barber.service;

import com.spring.barber.location.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findAllByLocation(Location location);
    boolean existsById(Long id);
}
