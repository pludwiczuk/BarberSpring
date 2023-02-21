package com.spring.barber.service;

import com.spring.barber.location.Location;
import com.spring.barber.location.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
public class ServiceController {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private LocationRepository locationRepository;

    @PostMapping("/services")
    Service create(@RequestBody @Valid CreateServiceDto createServiceDto) {
        Service service = createServiceDto.convertToEntity();
        Location location = locationRepository.findById(createServiceDto.getLocationId()).get();
        service.setLocation(location);
        return serviceRepository.save(service);
    }

    @GetMapping("/services/{id}")
    Optional<Service> getById(@PathVariable long id) {
        return serviceRepository.findById(id);
    }

    @PutMapping("/services/{serviceId}")
    ResponseEntity<?> update(@RequestBody @Valid CreateServiceDto serviceDto, @PathVariable long serviceId) {
        serviceService.updateService(serviceDto, serviceId);
        return ResponseEntity.accepted().body("Service updated successfully");
    }

    @DeleteMapping("/services/{serviceId}")
    ResponseEntity<?> delete(@PathVariable long serviceId) {
        serviceService.deleteService(serviceId);
        return ResponseEntity.accepted().body("Service deleted successfully");
    }

}
