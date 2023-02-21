package com.spring.barber.appointment;

import com.spring.barber.auth.AuthService;
import com.spring.barber.business.Business;
import com.spring.barber.businessUser.BusinessUser;
import com.spring.barber.businessUser.BusinessUserRepository;
import com.spring.barber.location.Location;
import com.spring.barber.service.ServiceRepository;
import com.spring.barber.timetable.TimetableService;
import com.spring.barber.user.Role;
import com.spring.barber.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.lang.reflect.Field;
import java.util.*;

import java.util.stream.Collectors;

@Service
public class AppointmentService {

  @Autowired
  private AppointmentRepository appointmentRepository;

  @Autowired
  private AuthService authService;

  @Autowired
  private ServiceRepository serviceRepository;

  @Autowired
  private BusinessUserRepository businessUserRepository;

  @Autowired
  private TimetableService timetableService;

  public Appointment create(CreateAppointmentDto dto) {
    Appointment appointment;
    if(dto.getDate() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'date' is null");
    }
    if(dto.getDate().isAfter(LocalDateTime.now().plusHours(1))) {
      Optional<com.spring.barber.service.Service> service = serviceRepository.findById(dto.getServiceId());
      if(service.isEmpty()) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Service not found");
      }
      List<String> possibleHours = timetableService.getPossibleHours(service.get().getLocation().getId(), dto.getDate().toLocalDate().toString());
      if(possibleHours.isEmpty()) {
        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Location is closed in that time");
      }
      boolean znalezionoDostepnyTermin = false;
      for(String hour : possibleHours) {
        if((dto.getDate().toLocalTime().equals(LocalTime.parse(hour)) || dto.getDate().toLocalTime().isAfter(LocalTime.parse(hour))) && dto.getDate().toLocalTime().isBefore(LocalTime.parse(hour).plusMinutes(30))) {
          znalezionoDostepnyTermin = true;
          break;
        }
      }
      if(znalezionoDostepnyTermin) {
        appointment = new Appointment(dto);
        appointment.setService(service.get());
        appointment.setUser(authService.getAuthenticatedUser());
        appointmentRepository.save(appointment);
      } else {
        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Selected date is taken");
      }
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Selected date is in the past");
    }
    return appointment;
  }

  public List<Appointment> getAppointmentsByUserId(long userId, Optional<String> date, Optional<String> status, boolean czyBusinessUser) {
    List<Appointment> appointmentList;
    if(czyBusinessUser) {
      Optional<BusinessUser> optionalBusinessUser = businessUserRepository.findBusinessUserByUserId(userId);
      if(optionalBusinessUser.isEmpty()) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Business not found");
      }
      appointmentList = new ArrayList<>();
      Business business = optionalBusinessUser.get().getBusiness();

      for(Location location : business.getLocations()) {
        for(com.spring.barber.service.Service service : location.getServices()) {
          appointmentList.addAll(service.getAppointments());
        }
      }
      appointmentList.sort((o1, o2) -> (int)(o1.getId()-o2.getId()));
    } else {
      appointmentList = appointmentRepository.findAllByUserId(userId);
    }
    if(date.isPresent()) {
      try {
        LocalDate localDate = LocalDate.parse(date.get());
        appointmentList = appointmentList.stream()
                .filter(appointment -> appointment.getDate().toLocalDate().isEqual(localDate))
                .collect(Collectors.toList());
      } catch(DateTimeParseException e) {
        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "'date' must be formatted like: yyyy-mm-dd");
      }
    }
    if(status.isPresent()) {
      String s = status.get();
      if(!s.equalsIgnoreCase("upcoming") && !s.equalsIgnoreCase("ended") && !s.equalsIgnoreCase("canceled")) {
        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "'status' must by must be one of: [upcoming, ended, canceled]");
      }
      if(s.equalsIgnoreCase("upcoming")) {
        appointmentList = appointmentList.stream()
                .filter(appointment -> LocalDateTime.now().isBefore(appointment.getDate()))
                .collect(Collectors.toList());
      } else if(s.equalsIgnoreCase("ended")) {
        appointmentList = appointmentList.stream()
                .filter(appointment -> LocalDateTime.now().isAfter(appointment.getDate()))
                .collect(Collectors.toList());
      } else {
        appointmentList = appointmentList.stream()
                .filter(appointment -> appointment.getStatus() == Status.CANCELED)
                .collect(Collectors.toList());
      }
    }
    return appointmentList;
  }

  public Appointment delete(long appointmentId) {
    Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentId);
    if(optionalAppointment.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Appointment not found");
    }
    Appointment appointment = optionalAppointment.get();
    User user = authService.getAuthenticatedUser();
    if(user.getRole() == Role.ROLE_BUSINESS_USER) {
      if(!businessUserRepository.existsByUserIdAndBusinessId(user.getId(), appointment.getService().getLocation().getBusiness().getId())) {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot canceled appointment from other location");
      }
      if (appointment.getStatus() == Status.CANCELED) {
        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "This appointment has already been canceled");
      }
      if (LocalDateTime.now().isBefore(appointment.getDate().minusHours(1))) {
        appointment.setStatus(Status.CANCELED);
        appointmentRepository.save(appointment);
      } else {
        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You cannot cancel appointment less than 1 hour before");
      }
    } else {
      if (!user.getUsername().equals(appointment.getUser().getUsername())) {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You have not permission for canceled this appointment");
      }
      if (appointment.getStatus() == Status.CANCELED) {
        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "This appointment has already been canceled");
      }
      if (LocalDateTime.now().isBefore(appointment.getDate().minusHours(1))) {
        appointment.setStatus(Status.CANCELED);
        appointmentRepository.save(appointment);
      } else {
        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You cannot cancel your appointment less than 1 hour before");
      }
    }
    return appointment;
  }

  public Appointment getAppointmentById(long appointmentId) {
    Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentId);
    if(optionalAppointment.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Appointment not found");
    }
    Appointment appointment = optionalAppointment.get();
    User user = authService.getAuthenticatedUser();
    if(user.getRole() == Role.ROLE_BUSINESS_USER) {
      if(!businessUserRepository.existsByUserIdAndBusinessId(user.getId(), appointment.getService().getLocation().getBusiness().getId())) {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot get appointment from other location");
      }
    } else if(user.getRole() == Role.ROLE_USER) {
      if(!appointment.getUser().getId().equals(user.getId())) {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can get only your appointments");
      }
    } else {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have required role to get this appointment");
    }
    return appointment;
  }

  public Appointment updateAppointment(Map<String, String> fields, Appointment appointment) {
    fields.forEach((key, value) -> {
      try {
        Field field = ReflectionUtils.findField(Appointment.class, key);
        field.setAccessible(true);
        switch (key) {
          case "date":
            LocalDateTime date = LocalDateTime.parse(value);
            List<String> possibleHours = timetableService.getPossibleHours(appointment.getService().getLocation().getId(), date.toLocalDate().toString());
            if(possibleHours.isEmpty()) {
              throw new Exception("You cannot update Appointment at this day, because Location is closed in that time or there are no more available appointments");
            }
            boolean znalezionoDostepnyTermin = false;
            for(String hour : possibleHours) {
              if((date.toLocalTime().equals(LocalTime.parse(hour)) || date.toLocalTime().isAfter(LocalTime.parse(hour))) && date.toLocalTime().isBefore(LocalTime.parse(hour).plusMinutes(30))) {
                znalezionoDostepnyTermin = true;
                break;
              }
            }
            if(znalezionoDostepnyTermin) {
              field.set(appointment, date);
            } else {
              throw new Exception("You cannot update Appointment at this time, because selected date is taken or Location is closed");
            }
            break;

          case "service":
            Optional<com.spring.barber.service.Service> optionalService = serviceRepository.findById(Long.parseLong(value));
            if (optionalService.isEmpty()) {
              throw new NoSuchElementException();
            }
            com.spring.barber.service.Service service = optionalService.get();
            field.set(appointment, service);
            break;

          case "status":
            if(authService.getAuthenticatedUser().getRole() == Role.ROLE_USER) {
              if (value.equalsIgnoreCase("current")) {
                if(appointment.getStatus() == Status.CANCELED) {
                  if(appointment.getDate().isAfter(LocalDateTime.now().plusHours(1))) {
                    possibleHours = timetableService.getPossibleHours(appointment.getService().getLocation().getId(), appointment.getDate().toLocalDate().toString());
                    if(possibleHours.isEmpty()) {
                      throw new Exception("You can`t change status of this Appointment, because this date is taken already");
                    }
                    znalezionoDostepnyTermin = false;
                    for(String hour : possibleHours) {
                      if((appointment.getDate().toLocalTime().equals(LocalTime.parse(hour)) || appointment.getDate().toLocalTime().isAfter(LocalTime.parse(hour))) && appointment.getDate().toLocalTime().isBefore(LocalTime.parse(hour).plusMinutes(30))) {
                        znalezionoDostepnyTermin = true;
                        break;
                      }
                    }
                    if(znalezionoDostepnyTermin) {
                      field.set(appointment, Status.CURRENT);
                    } else {
                      throw new Exception("You can`t change status of this Appointment, because selected date is taken already");
                    }
                  } else {
                    throw new Exception("You can`t change 'status', because this Appointment is in the past");
                  }
                }
              } else if(value.equalsIgnoreCase("canceled")) {
                if(appointment.getStatus() == Status.CURRENT) {
                  if (LocalDateTime.now().isBefore(appointment.getDate().minusHours(1))) {
                    field.set(appointment, Status.CANCELED);
                  } else {
                    throw new Exception("You cannot cancel your Appointment less than 1 hour before");
                  }
                }
              } else {
                throw new Exception("'status' must be one of: [current, canceled]");
              }
            } else {
              if (value.equalsIgnoreCase("current")) field.set(appointment, Status.CURRENT);
              else if (value.equalsIgnoreCase("canceled")) field.set(appointment, Status.CANCELED);
              else throw new Exception("'status' must be one of: [current, canceled]");
            }
            break;
        }
      } catch (DateTimeParseException e2) {
        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "'date' must be formatted like: yyyy-mm-ddThh:mm:ss");
      } catch (NoSuchElementException e3) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "'service' not found");
      } catch(Exception e) {
        // incorrected status
        if(e.getMessage() != null) {
          throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        } // or ignore non existing fields in user
      }
    });
    return appointmentRepository.save(appointment);
  }

  public List<Appointment> getUpcominAppointments(long userId, boolean czyBusinessUser) {
    List<Appointment> appointmentList;
    if(czyBusinessUser) {
      Optional<BusinessUser> optionalBusinessUser = businessUserRepository.findBusinessUserByUserId(userId);
      if(optionalBusinessUser.isEmpty()) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Business not found");
      }
      appointmentList = new ArrayList<>();
      Business business = optionalBusinessUser.get().getBusiness();

      for(Location location : business.getLocations()) {
        for(com.spring.barber.service.Service service : location.getServices()) {
          appointmentList.addAll(service.getAppointments());
        }
      }
      appointmentList.sort((o1, o2) -> (int)(o1.getId()-o2.getId()));
    } else {
      appointmentList = appointmentRepository.findAllByUserId(userId);
    }
    appointmentList = appointmentList.stream()
            .filter(appointment -> LocalDateTime.now().isBefore(appointment.getDate()))
            .filter(appointment -> appointment.getStatus() == Status.CURRENT)
            .collect(Collectors.toList());
    return appointmentList;
  }
}
