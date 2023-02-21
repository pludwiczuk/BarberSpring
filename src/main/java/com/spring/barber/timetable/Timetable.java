package com.spring.barber.timetable;

import com.spring.barber.location.Location;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "timetables")
public class Timetable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "location_id")
  private Location location;

  @NotNull
  private DayOfWeek dayOfWeek;

  @NotNull
  private LocalTime openAt;

  @NotNull
  private LocalTime closeAt;

  @NotNull
  private boolean wholeDayOpen;

  @NotNull
  private boolean wholeDayClosed;

}
