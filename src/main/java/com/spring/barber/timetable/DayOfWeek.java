package com.spring.barber.timetable;

public enum DayOfWeek {

  MONDAY("MONDAY"),
  TUESDAY("TUESDAY"),
  WEDNESDAY("WEDNESDAY"),
  THURSDAY("THURSDAY"),
  FRIDAY("FRIDAY"),
  SATURDAY("SATURDAY"),
  SUNDAY("SUNDAY");

  private String value;

  private DayOfWeek(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return this.value;
  }

  static public DayOfWeek get(String dayOfWeek) {
    switch (dayOfWeek) {
      case "MONDAY": return DayOfWeek.MONDAY;
      case "TUESDAY": return DayOfWeek.TUESDAY;
      case "WEDNESDAY": return DayOfWeek.WEDNESDAY;
      case "THURSDAY": return DayOfWeek.THURSDAY;
      case "FRIDAY": return DayOfWeek.FRIDAY;
      case "SATURDAY": return DayOfWeek.SATURDAY;
      case "SUNDAY": return DayOfWeek.SUNDAY;
      default: throw new RuntimeException("Unknown day of week");
    }
  }

  static public DayOfWeek next(DayOfWeek dayOfWeek) {
    switch (dayOfWeek) {
      case MONDAY: return DayOfWeek.TUESDAY;
      case TUESDAY: return DayOfWeek.WEDNESDAY;
      case WEDNESDAY: return DayOfWeek.THURSDAY;
      case THURSDAY: return DayOfWeek.FRIDAY;
      case FRIDAY: return DayOfWeek.SATURDAY;
      case SATURDAY: return DayOfWeek.SUNDAY;
      case SUNDAY: return DayOfWeek.MONDAY;
      default: throw new RuntimeException("Unknown day of week");
    }
  }

  static public DayOfWeek previous(DayOfWeek dayOfWeek) {
    switch (dayOfWeek) {
      case MONDAY: return DayOfWeek.SUNDAY;
      case TUESDAY: return DayOfWeek.MONDAY;
      case WEDNESDAY: return DayOfWeek.TUESDAY;
      case THURSDAY: return DayOfWeek.WEDNESDAY;
      case FRIDAY: return DayOfWeek.THURSDAY;
      case SATURDAY: return DayOfWeek.FRIDAY;
      case SUNDAY: return DayOfWeek.SATURDAY;
      default: throw new RuntimeException("Unknown day of week");
    }
  }

}
