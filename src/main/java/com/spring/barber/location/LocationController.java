package com.spring.barber.location;

import com.spring.barber.auth.AuthService;
import com.spring.barber.exception.ForbiddenException;
import com.spring.barber.service.Service;
import com.spring.barber.service.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class LocationController {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LocationService locationService;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private AuthService authService;

    @PostMapping("/locations")
    ResponseEntity<?> createLocation(@RequestBody @Valid CreateLocationDto dto) {
        Location location = locationService.createLocation(dto);
        return new ResponseEntity<>(new LocationResponse(location), HttpStatus.CREATED);
    }

    @GetMapping("/locations")
    ResponseEntity<?> getAllLocations(@RequestParam(required = false) String city) {
        List<Location> locations = null;
        if (city != null) {
            locations = locationRepository.findAllByAddressCityContaining(city);
        }
        else {
            locations = locationRepository.findAll();
        }
        return new ResponseEntity<>(locations, HttpStatus.OK);
    }

    @GetMapping("/businesses/{businessId}/locations")
    ResponseEntity<?> getAllBusinessLocations(@PathVariable long businessId) {
        if (!authService.hasAccessToBusiness(businessId)) {
            throw new ForbiddenException();
        }
        List<Location> locations = locationRepository.findAllByBusinessId(businessId);
        return new ResponseEntity<>(locations, HttpStatus.OK);
    }

    @GetMapping("/locations/{locationId}")
    ResponseEntity<?> getById(@PathVariable long locationId) {
        Optional<Location> location = locationRepository.findById(locationId);
        if (!location.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found");
        }
        return new ResponseEntity<>(location.get(), HttpStatus.OK);
    }

    @PutMapping("/locations/{id}")
    ResponseEntity<?> put(@RequestBody @Valid CreateLocationDto locationDto, @PathVariable long id) {
        Optional<Location> locationOptional = locationRepository.findById(id);
        if (locationOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Location location = locationService.replaceLocation(locationDto, id);
        return ResponseEntity.accepted().body(new LocationResponse(location));
    }

    @DeleteMapping("/locations/{locationId}")
    ResponseEntity<?> delete(@PathVariable long locationId) {
        locationService.deleteLocation(locationId);
        return ResponseEntity.accepted().body("Location deleted successfully");
    }

    @GetMapping("/locations/{id}/services")
    ResponseEntity<List<Service>> getServices(@PathVariable long id) {
        Optional<Location> locationOptional = locationRepository.findById(id);
        if (!locationOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Location location = locationOptional.get();
        return ResponseEntity.ok().body(serviceRepository.findAllByLocation(location));
    }

    @PatchMapping("/locations/{locationId}")
    ResponseEntity<?> updateLocation(@RequestBody Map<String, String> fields, @PathVariable long locationId) {
        if(fields.containsKey("id")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "'id' field is forbidden");
        } else if(fields.containsKey("services")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "'services' field is forbidden");
        } else if(fields.containsKey("timetables")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "'timetables' field is forbidden");
        }
        Optional<Location> optionalLocation = locationRepository.findById(locationId);
        if (optionalLocation.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found");
        }
        Location location = optionalLocation.get();
        Location updateLocation = locationService.updateLocation(fields, location);
        return new ResponseEntity<>(updateLocation, HttpStatus.OK);
    }

}
