package com.spring.barber.businessUser;

import java.util.List;
import java.util.Optional;

import com.spring.barber.appointment.Appointment;
import com.spring.barber.auth.AuthService;
import com.spring.barber.business.Business;
import com.spring.barber.business.BusinessRepository;
import com.spring.barber.location.Location;
import com.spring.barber.location.LocationService;
import com.spring.barber.user.User;
import com.spring.barber.user.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class BusinessUserService {

  @Autowired
  private BusinessUserRepository businessUserRepository;

  @Autowired
  private BusinessRepository businessRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private AuthService authService;

  @Autowired
  private LocationService locationService;

  public BusinessUser createBusinessUser(Business business, User user) {
    BusinessUser businessUser = new BusinessUser();
    businessUser.setBusiness(business);
    businessUser.setUser(user);
    businessUserRepository.save(businessUser);
    return businessUser;
  }

  public BusinessUser createBusinessUser(CreateBusinessUserDto dto) {
    BusinessUser businessUser = new BusinessUser();
    businessUser.setBusiness(businessRepository.findById(dto.getBusinessId()).get());
    businessUser.setUser(userRepository.findById(dto.getUserId()).get());
    businessUserRepository.save(businessUser);
    return businessUser;
  }

  public List<BusinessUser> findAllByBusinessId(Long businessId) {
    return businessUserRepository.findAllByBusinessId(businessId);
  }

  public List<BusinessUser> findAllByUserId(Long userId) {
    return businessUserRepository.findAllByUserId(userId);
  }

  public BusinessUser findOneByUserId(Long userId) {
    return businessUserRepository.findOneByUserId(userId);
  }

  public boolean hasAccessToBusiness(Long businessId) {
    User user = authService.getAuthenticatedUser();
    return businessUserRepository.existsByUserIdAndBusinessId(user.getId(), businessId);
  }

  public boolean hasAccessToBusiness(Business business) {
    return hasAccessToBusiness(business.getId());
  }

  public boolean hasAccessToLocation(Location location) {
    return hasAccessToBusiness(location.getBusiness());
  }

  public boolean hasAccessToService(com.spring.barber.service.Service service) {
    return hasAccessToBusiness(service.getLocation().getBusiness());
  }

  public boolean hasAccessToAppointment(Appointment appointment) {
    return hasAccessToBusiness(appointment.getService().getLocation().getBusiness());
  }

  public List<Location> getAllLocations(long userId) {
    Optional<BusinessUser> optionalBusinessUser = businessUserRepository.findBusinessUserByUserId(userId);
    if(optionalBusinessUser.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Business user not find");
    }
    long businessId = optionalBusinessUser.get().getBusiness().getId();
    List<Location> locationList = locationService.getLocationsByBusinessId(businessId);
    return  locationList;
  }
}
