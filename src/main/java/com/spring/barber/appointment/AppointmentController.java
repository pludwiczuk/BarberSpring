package com.spring.barber.appointment;

import com.spring.barber.auth.AuthService;
import com.spring.barber.business.Business;
import com.spring.barber.businessUser.BusinessUser;
import com.spring.barber.businessUser.BusinessUserRepository;
import com.spring.barber.exception.ForbiddenException;
import com.spring.barber.user.Role;
import com.spring.barber.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Map;

@RestController
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private AuthService authService;

    @Autowired
    private BusinessUserRepository businessUserRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @PostMapping("/appointments")
    ResponseEntity<?> create(@RequestBody @Valid CreateAppointmentDto dto) {
        Appointment appointment = appointmentService.create(dto);
        return new ResponseEntity<>(new AppointmentResponse(appointment), HttpStatus.CREATED);
    }

    @DeleteMapping("/appointments/{appointmentId}")
    ResponseEntity<?> delete(@PathVariable long appointmentId) {
        Appointment appointment = appointmentService.delete(appointmentId);
        return new ResponseEntity<>(new AppointmentResponse(appointment), HttpStatus.OK);
    }

    @GetMapping("/appointments")
    ResponseEntity<?> getAppointments(@RequestParam(required = false) Optional<String> date, @RequestParam(required = false) Optional<String> status) {
        User user = authService.getAuthenticatedUser();
        if(user == null) {
            throw new ForbiddenException();
        }
        List<Appointment> appointmentList;
        if(user.getRole() == Role.ROLE_USER) {
            appointmentList = appointmentService.getAppointmentsByUserId(user.getId(), date, status, false);
        } else {
            appointmentList = appointmentService.getAppointmentsByUserId(user.getId(), date, status, true);
        }

        List<AppointmentResponse> appointmentResponseList = new ArrayList<>();
        appointmentList.forEach(a -> appointmentResponseList.add(new AppointmentResponse(a)));
        return new ResponseEntity<>(appointmentResponseList, HttpStatus.OK);
    }

    @GetMapping("/appointments/{appointmentId}")
    ResponseEntity<?> getAppointmentById(@PathVariable long appointmentId) {
        Appointment appointment = appointmentService.getAppointmentById(appointmentId);
        return new ResponseEntity<>(new AppointmentResponse(appointment), HttpStatus.OK);
    }

    @PatchMapping("/appointments/{appointmentId}")
    ResponseEntity<?> updateAppointment(@RequestBody Map<String, String> fields, @PathVariable long appointmentId) {
        if(fields.containsKey("id")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "'id' field is forbidden");
        } else if(fields.containsKey("user")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "'user' field is forbidden");
        }
        User user = authService.getAuthenticatedUser();
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentId);
        if(optionalAppointment.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Appointment not found");
        }
        Appointment appointment = optionalAppointment.get();
        if(user.getRole() == Role.ROLE_USER) {
            if(!appointment.getUser().getId().equals(user.getId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can update only your appointments");
            }
        } else {
            Optional<BusinessUser> optionalBusinessUser = businessUserRepository.findBusinessUserByUserId(user.getId());
            if(optionalBusinessUser.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Business not found");
            }
            Business business = optionalBusinessUser.get().getBusiness();
            if(!business.getLocations().contains(appointment.getService().getLocation())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This appointment is from other business. You can`t update it");
            }
        }
        Appointment appointmentUpdateResp = appointmentService.updateAppointment(fields, appointment);
        return new ResponseEntity<>(new AppointmentResponse(appointmentUpdateResp), HttpStatus.OK);
    }

    @GetMapping("/appointments/upcoming")
    ResponseEntity<?> getUpcomingAppointments() {
        User user = authService.getAuthenticatedUser();
        if(user == null) {
            throw new ForbiddenException();
        }
        List<Appointment> appointmentList;
        if(user.getRole() == Role.ROLE_USER) {
            appointmentList = appointmentService.getUpcominAppointments(user.getId(), false);
        } else {
            appointmentList = appointmentService.getUpcominAppointments(user.getId(), true);
        }

        return new ResponseEntity<>(appointmentList.stream().map(AppointmentResponse::new), HttpStatus.OK);
    }

}
