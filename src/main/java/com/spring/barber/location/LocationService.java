package com.spring.barber.location;

import com.spring.barber.address.Address;
import com.spring.barber.auth.AuthService;
import com.spring.barber.business.Business;
import com.spring.barber.business.BusinessService;
import com.spring.barber.businessUser.BusinessUserService;
import com.spring.barber.service.ServiceService;
import com.spring.barber.timetable.*;
import com.spring.barber.user.Role;
import com.spring.barber.user.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class LocationService {

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private BusinessService businessService;

  @Autowired
  private AuthService authService;

  @Autowired
  private BusinessUserService businessUserService;
  
  @Autowired
  private TimetableService timetableService;

  @Autowired
  private TimetableRepository timetableRepository;

  @Autowired
  private ServiceService serviceService;
  

  public Location createLocation(CreateLocationDto dto) {
    Location location = new Location(dto);
    location.setBusiness(businessService.getById(dto.getBusinessId()));
    if (!businessUserService.hasAccessToBusiness(dto.getBusinessId())) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have access to business");  
    }
    locationRepository.save(location);

    DayOfWeek day = DayOfWeek.MONDAY;
    do {
      Timetable timetable = DefaultTimetableFactory.build(location, day);
      timetableService.createTimetable(timetable);
      day = DayOfWeek.next(day);
    } while (!day.equals(DayOfWeek.MONDAY));
    
    return location;
  }

  public Location replaceLocation(CreateLocationDto locationDto, long id) {
    Location location = locationRepository.findById(id).get();

    if(!authService.hasAccessToBusiness(location.getBusiness().getId())) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have access to business");
    }

    location.setName(locationDto.getName());
    location.setAddress(new Address(locationDto.getAddress()));
    try {
      Business business = businessService.getById(locationDto.getBusinessId());
      if(!authService.hasAccessToBusiness(business.getId())) {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cant update 'business'. You do not have access to new business");
      }
      location.setBusiness(business);
      locationRepository.save(location);
    } catch (NoSuchElementException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Business not found");
    }
    return location;
  }

  public void deleteLocation(long locationId) {
    Optional<Location> optionalLocation = locationRepository.findById(locationId);
    if (optionalLocation.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found");
    }
    Location location = optionalLocation.get();

    if (!authService.hasAccessToBusiness(location.getBusiness().getId())) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN,
              "You do not have permission to delete location: " + location.getName());
    }

    // Operating on List copy because elements of original List are being removed,
    // which would interrupt iteration
    List<com.spring.barber.service.Service> servicesToDelete = new ArrayList<>(location.getServices());
    for (com.spring.barber.service.Service service : servicesToDelete) {
      serviceService.deleteService(service.getId());
    }

    // Operating on List copy because elements of original List are being removed,
    // which would interrupt iteration
    List<Timetable> timetablesToDelete = new ArrayList<>(location.getTimetables());
    for (Timetable timetable : timetablesToDelete) {
      location.getTimetables().remove(timetable);
      timetableRepository.delete(timetable);
    }

    locationRepository.delete(location);
  }

  public Location getById(Long locationId) {
    return locationRepository.findById(locationId).get();
  }

  public Location updateLocation(Map<String, String> fields, Location location) {
    User user = authService.getAuthenticatedUser();
    if(user.getRole() == Role.ROLE_BUSINESS_USER) {
      if(!authService.hasAccessToBusiness(location.getBusiness().getId())) {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have access to business");
      }
      fields.forEach((key, value) -> {
        try {
          Field field;
          if (key.equals("line1") || key.equals("line2") || key.equals("postCode") || key.equals("city") || key.equals("country")) {
            field = ReflectionUtils.findField(Location.class, "address");
            field.setAccessible(true);
            Address address = location.getAddress();
            if (address == null) {
              address = new Address();
            }
            if (key.equals("line2")) {
              Field f2 = ReflectionUtils.findField(Address.class, key);
              f2.setAccessible(true);
              f2.set(address, value);
            } else {
              Field f2 = ReflectionUtils.findField(Address.class, key);
              f2.setAccessible(true);
              if (value.length() > 0) {
                f2.set(address, value);
              } else {
                throw new Exception("'" + key + "' must not be empty");
              }
            }
            field.set(location, address);
          } else {
            field = ReflectionUtils.findField(Location.class, key);
            field.setAccessible(true);
            switch (key) {

              case "name":
                if (value.length() == 0) {
                  throw new Exception("'name' must not be empty");
                }
                field.set(location, value);
                break;

              case "business":
                try {
                  Business business = businessService.getById(Long.parseLong(value));
                  if(!authService.hasAccessToBusiness(business.getId())) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cant update 'business'. You do not have access to this business");
                  }
                  field.set(location, business);
                } catch(NoSuchElementException e) {
                  throw new ResponseStatusException(HttpStatus.NOT_FOUND, "'business' not found");
                }
                break;
            }
          }
        } catch (NullPointerException e) {
          if(e.getMessage() != null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
          }
        } catch(ResponseStatusException e) {
          throw e;
        } catch(Exception e) {
          if(e.getMessage() != null) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
          }
        }
      });
    } else {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You have not required role");
    }
    locationRepository.save(location);
    return location;
  }

  public List<Location> getLocationsByBusinessId(long businessId) {
    List<Location> locationList = locationRepository.findAllByBusinessId(businessId);
    return locationList;
  }

}
