package com.spring.barber.appointment;

import com.spring.barber.service.ServiceExist;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import java.time.LocalDateTime;

interface ValidationStepOne {}
interface ValidationStepTwo {}

@Getter
@Setter
public class CreateAppointmentDto {

    @ServiceExist(groups = ValidationStepTwo.class)
    @Positive(message = "'serviceId' must be positive integer", groups = ValidationStepOne.class)
    @NotNull(message = "'serviceId' cannot be null", groups = ValidationStepOne.class)
    private long serviceId;

    @Future(groups = ValidationStepOne.class)
    @DateTimeFormat
    @NotNull(message = "'date' cannot be null", groups = ValidationStepOne.class)
    private LocalDateTime date;

}
