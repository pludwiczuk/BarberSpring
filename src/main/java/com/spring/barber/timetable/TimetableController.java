package com.spring.barber.timetable;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TimetableController {

  @Autowired
  private TimetableService timetableService;

  @GetMapping("/locations/{locationId}/timetable")
  ResponseEntity<?> getLocationTimetables(@PathVariable long locationId) {
    List<TimetableResponse> response = timetableService.getLocationTimetables(locationId)
      .stream()
      .map(t -> new TimetableResponse(t))
      .collect(Collectors.toList());
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PatchMapping("locations/{locationId}/timetable")
  ResponseEntity<?> updateLocationTimetable(@PathVariable long locationId, @RequestBody @Valid UpdateTimetableDto dto) {
    Timetable timetable = timetableService.updateTimetable(locationId, dto);
    return new ResponseEntity<>(new TimetableResponse(timetable), HttpStatus.OK);
  }

  @GetMapping("/locations/{locationId}/possiblehours/{date}")
  ResponseEntity<?> getPossibleHours(@PathVariable long locationId, @PathVariable String date) {
    List<String> response = timetableService.getPossibleHours(locationId, date);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

}
