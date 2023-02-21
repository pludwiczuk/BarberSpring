package com.spring.barber.exception;

public class ForbiddenException extends RuntimeException {

  public ForbiddenException() {
    super("You do not have access to this resource");
  }

}
