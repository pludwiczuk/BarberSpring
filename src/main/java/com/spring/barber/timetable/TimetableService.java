package com.spring.barber.timetable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.spring.barber.appointment.Appointment;
import com.spring.barber.appointment.AppointmentRepository;
import com.spring.barber.appointment.Status;
import com.spring.barber.businessUser.BusinessUserService;
import com.spring.barber.location.Location;
import com.spring.barber.location.LocationRepository;
import com.spring.barber.location.LocationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TimetableService {

  @Autowired
  private TimetableRepository timetableRepository;

  @Autowired
  private LocationService locationService;

  @Autowired
  private BusinessUserService businessUserService;

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private AppointmentRepository appointmentRepository;

  public Timetable createTimetable(Timetable timetable) {
    timetableRepository.save(timetable);
    return timetable;
  }

  public List<Timetable> getLocationTimetables(long locationId) {
    return timetableRepository.findAllByLocationId(locationId);
  }

  public Timetable updateTimetable(long locationId, UpdateTimetableDto dto) {
    Location location = locationService.getById(locationId);
    if (location == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found");
    }
    if (!businessUserService.hasAccessToLocation(location)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You have do not have access to this location");
    }
    Timetable timetable = timetableRepository.findByLocationIdAndDayOfWeek(locationId, DayOfWeek.get(dto.getDayOfWeek().toUpperCase()));
    timetable.setOpenAt(LocalTime.parse(dto.getOpenAt()));
    timetable.setCloseAt(LocalTime.parse(dto.getCloseAt()));
    timetable.setWholeDayOpen(dto.isWholeDayOpen());
    timetable.setWholeDayClosed(dto.isWholeDayClosed());
    timetableRepository.save(timetable);
    return timetable;
  }

  public List<String> getPossibleHours(long locationId, String date) {
    if(!locationRepository.existsById(locationId)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found");
    }

    List<Timetable> timetableList = this.getLocationTimetables(locationId);
    if(timetableList == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Timetables of this location not found");
    } else if(timetableList.size() == 0) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Timetables of this location not found");
    }

    LocalDate localDate;
    try {
      localDate = LocalDate.parse(date);
    } catch (DateTimeParseException e) {
      throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "'date' must be formatted like: yyyy-mm-dd");
    }
    List<Appointment> appointmentList = appointmentRepository.findAllByDateBetween(localDate.atStartOfDay(), localDate.atTime(LocalTime.MAX));
    List<Appointment> appointmentListOfLocation = appointmentList.stream()
            .filter(appointment -> appointment.getService().getLocation().getId() == locationId)
            .filter(appointment -> appointment.getStatus() == Status.CURRENT)
            .collect(Collectors.toList());
    Optional<Timetable> optionalTimetable = timetableList.stream()
            .filter(timetable1 -> timetable1.getDayOfWeek().toString().equals(localDate.getDayOfWeek().toString()))
            .findFirst();
    if(optionalTimetable.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Timetable of this day not found");
    }
    Timetable timetable = optionalTimetable.get();
    List<String> respons = new ArrayList<>();

    List<String> busyList = new ArrayList<>();
    LocalTime poz;
    LocalTime start;
    LocalTime stop;

    if(timetable.isWholeDayClosed()) {
      return respons;
    } else if(timetable.isWholeDayOpen()) {
      start = LocalTime.MIDNIGHT;
      stop = LocalTime.MAX;
    } else {
      start = timetable.getOpenAt();
      stop = timetable.getCloseAt();
    }

    for(Appointment appointment : appointmentListOfLocation) {
      poz = start;
      while (poz.isBefore(stop)) {
        if ((appointment.getDate().toLocalTime().equals(poz) || appointment.getDate().toLocalTime().isAfter(poz)) && appointment.getDate().toLocalTime().isBefore(poz.plusMinutes(30))) {
          if(!busyList.contains(poz.toString())) {
            busyList.add(poz.toString());
          }
        }
        poz = poz.plusMinutes(30);
      }
    }
    poz = start;
    while(poz.isBefore(stop)) {
      if(!busyList.contains(poz.toString())) {
        respons.add(poz.toString());
      }
      poz = poz.plusMinutes(30);
    }

    return respons;
  }

}
