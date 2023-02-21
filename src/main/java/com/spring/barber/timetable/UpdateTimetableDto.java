package com.spring.barber.timetable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTimetableDto {

  @Pattern(
    regexp = "(monday|tuesday|wednesday|thursday|friday|saturday|sunday)$",
    message = "'dayOfWeek' must be one of: [monday, tuesday, wednesday, thursday, friday, saturday, sunday]")
  @NotEmpty(message = "'dayOfWeek' must not be empty")
  private String dayOfWeek;

  @Pattern(
    regexp = "^([01]\\d?|2[0-4]):[0-5]\\d(:[0-5]\\d)?$",
    message = "'openAt' must be in format 'HH:mm'")
  @NotEmpty(message = "'openAt' must not be empty")
  private String openAt;

  @Pattern(
    regexp = "^([01]\\d?|2[0-4]):[0-5]\\d(:[0-5]\\d)?$",
    message = "'closeAt' must be in format 'HH:mm'")
  @NotEmpty(message = "'closeAt' must not be empty")
  private String closeAt;

  @NotNull
  private boolean wholeDayOpen;

  @NotNull
  private boolean wholeDayClosed;

}
