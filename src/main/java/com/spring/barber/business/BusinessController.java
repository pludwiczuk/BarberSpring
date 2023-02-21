package com.spring.barber.business;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BusinessController {

    @Autowired
    private BusinessService businessService;

    @PostMapping("/businesses")
    ResponseEntity<?> createBusiness(@RequestBody @Valid CreateBusinessDto dto) {
        Business business = businessService.createBusiness(dto);
        return new ResponseEntity<Business>(business, HttpStatus.CREATED);
    }

    @GetMapping("/businesses/{businessId}")
    ResponseEntity<?> getBusiness(@PathVariable long businessId) {
        Business business = businessService.getBusiness(businessId);
        return ResponseEntity.accepted().body(business);
    }

    @PutMapping("/businesses/{businessId}")
    ResponseEntity<?> updateBusiness(
            @PathVariable long businessId,
            @RequestBody CreateBusinessDto businessDto) {
        businessService.updateBusiness(businessId, businessDto);
        return ResponseEntity.accepted().body("Business updated successfully");
    }

    @DeleteMapping("/businesses/{businessId}")
    ResponseEntity<?> deleteBusiness(@PathVariable long businessId) {
        businessService.deleteBusiness(businessId);
        return ResponseEntity.accepted().body("Business deleted successfully");
    }

}
