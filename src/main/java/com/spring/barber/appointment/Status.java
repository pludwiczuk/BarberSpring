package com.spring.barber.appointment;

public enum Status {

    CURRENT("CURRENT"),
    CANCELED("CANCELED");

    private String value;

    Status(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

}
