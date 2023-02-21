package com.spring.barber.business;

import com.spring.barber.auth.AuthService;
import com.spring.barber.businessUser.BusinessUser;
import com.spring.barber.businessUser.BusinessUserService;
import com.spring.barber.location.Location;
import com.spring.barber.location.LocationService;
import com.spring.barber.user.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BusinessService {

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private BusinessUserService businessUserService;

    @Autowired
    private LocationService locationService;


    public Business createBusiness(CreateBusinessDto dto) {
        Business business = new Business(dto);
        businessRepository.save(business);
        User authenticatedUser = authService.getAuthenticatedUser();
        businessUserService.createBusinessUser(business, authenticatedUser);
        return business;
    }

    public Business getById(long businessId) {
        return businessRepository.findById(businessId).get();
    }

    public Business getBusiness(long businessId) {
        Optional<Business> optionalBusiness = businessRepository.findById(businessId);
        if (optionalBusiness.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Business not found");
        }
        return optionalBusiness.get();
    }

    public void updateBusiness(long businessId, CreateBusinessDto businessDto) {
        Optional<Business> optionalBusiness = businessRepository.findById(businessId);
        if (optionalBusiness.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Business not found");
        }
        Business oldBusiness = optionalBusiness.get();

        if (!businessUserService.hasAccessToBusiness(oldBusiness)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You do not have permission to update this business");
        }

        Business updatedBusiness = new Business(businessDto);
        updatedBusiness.setId(oldBusiness.getId());
        updatedBusiness.setBusinessUsers(oldBusiness.getBusinessUsers());
        updatedBusiness.setLocations(oldBusiness.getLocations());

        businessRepository.save(updatedBusiness);
    }

    public void deleteBusiness(long businessId) {
        Optional<Business> optionalBusiness = businessRepository.findById(businessId);
        if (optionalBusiness.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Business not found");
        }
        Business business = optionalBusiness.get();

        if (!businessUserService.hasAccessToBusiness(business)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You do not have permission to delete this business");
        }

        List<Location> locationsToDelete = new ArrayList<>(business.getLocations());
        for (Location location : locationsToDelete) {
            locationService.deleteLocation(location.getId());
        }

        for (BusinessUser businessUser : business.getBusinessUsers()) {
            businessUser.setBusiness(null);
        }

        businessRepository.delete(business);
    }

}
