package com.spring.barber.timetable;

import java.time.LocalTime;

import com.spring.barber.location.Location;

public class DefaultTimetableFactory {

  public static Timetable build(Location location, DayOfWeek dayOfWeek) {
    Timetable timetable = new Timetable();
    timetable.setLocation(location);
    timetable.setDayOfWeek(dayOfWeek);
    timetable.setOpenAt(LocalTime.of(0, 0));
    timetable.setCloseAt(LocalTime.of(0, 0));
    timetable.setWholeDayOpen(false);
    timetable.setWholeDayClosed(true);
    return timetable;
  }

}
