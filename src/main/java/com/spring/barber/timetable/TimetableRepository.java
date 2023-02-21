package com.spring.barber.timetable;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TimetableRepository extends JpaRepository<Timetable, Long> {

  List<Timetable> findAllByLocationId(Long locationId);
  Timetable findByLocationIdAndDayOfWeek(Long locationId, DayOfWeek dayOfWeek);

}