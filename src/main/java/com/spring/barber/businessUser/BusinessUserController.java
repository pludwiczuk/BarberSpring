package com.spring.barber.businessUser;

import java.util.LinkedList;
import java.util.List;

import javax.validation.Valid;

import com.spring.barber.auth.AuthService;
import com.spring.barber.exception.ForbiddenException;

import com.spring.barber.location.Location;
import com.spring.barber.location.LocationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class BusinessUserController {

  @Autowired
  private AuthService authService;

  @Autowired
  private BusinessUserService businessUserService;

  @PostMapping("/business-users")
  ResponseEntity<?> addUserToBusiness(@RequestBody @Valid CreateBusinessUserDto dto) {
    if (!authService.hasAccessToBusiness(dto.getBusinessId())) {
      throw new ForbiddenException();
    }
    BusinessUser businessUser = businessUserService.createBusinessUser(dto);
    return new ResponseEntity<>(new BusinessUserResponse(businessUser), HttpStatus.CREATED);
  }

  @GetMapping("/businesses/{businessId}/business-users")
  ResponseEntity<?> getAllBusinessUsers(@PathVariable long businessId) {
    if (!authService.hasAccessToBusiness(businessId)) {
      throw new ForbiddenException();
    }
    List<BusinessUser> businessUsers = businessUserService.findAllByBusinessId(businessId);
    List<BusinessUserResponse> businessUserResponses = new LinkedList<>();
    businessUsers.forEach(bu -> businessUserResponses.add(new BusinessUserResponse(bu)));
    return new ResponseEntity<List<BusinessUserResponse>>(businessUserResponses, HttpStatus.OK);
  }

  @GetMapping("/users/{userId}/locations")
  ResponseEntity<?> getAllLocations(@PathVariable long userId) {
    List<Location> locations = businessUserService.getAllLocations(userId);
    return new ResponseEntity<>(locations.stream().map(LocationResponse::new),HttpStatus.OK);
  }

}
