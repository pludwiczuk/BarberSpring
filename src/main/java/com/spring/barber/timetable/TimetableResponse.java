package com.spring.barber.timetable;

import java.time.format.DateTimeFormatter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimetableResponse {

  private String dayOfWeek;
  private String openAt;
  private String closeAt;
  private boolean wholeDayOpen;
  private boolean wholeDayClosed;

  public TimetableResponse(Timetable timetable) {
    this.dayOfWeek = timetable.getDayOfWeek().toString().toLowerCase();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    this.openAt = formatter.format(timetable.getOpenAt());
    this.closeAt = formatter.format(timetable.getCloseAt());
    this.wholeDayOpen = timetable.isWholeDayOpen();
    this.wholeDayClosed = timetable.isWholeDayClosed();
  }

}
