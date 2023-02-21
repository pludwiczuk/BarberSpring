package com.spring.barber.service;

import com.spring.barber.appointment.Appointment;
import com.spring.barber.appointment.AppointmentService;
import com.spring.barber.businessUser.BusinessUserService;
import com.spring.barber.location.Location;
import com.spring.barber.location.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private BusinessUserService businessUserService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private LocationRepository locationRepository;

    public void updateService(CreateServiceDto serviceDto, long serviceId) {
        Optional<com.spring.barber.service.Service> optionalService = serviceRepository.findById(serviceId);
        if (optionalService.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Service not found");
        }
        var oldService = optionalService.get();

        if (!businessUserService.hasAccessToService(oldService)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You do not have permission to update service: " + oldService.getName());
        }

        Optional<Location> optionalLocation = locationRepository.findById(serviceDto.getLocationId());
        if (optionalLocation.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "No Location with matching id found");
        }
        var newLocation = optionalLocation.get();

        var updatedService = serviceDto.convertToEntity();
        updatedService.setId(oldService.getId());
        updatedService.setLocation(newLocation);
        updatedService.setAppointments(oldService.getAppointments());

        serviceRepository.save(updatedService);
    }

    public void deleteService(long serviceId) {
        Optional<com.spring.barber.service.Service> optionalService = serviceRepository.findById(serviceId);
        if (optionalService.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Service not found");
        }
        com.spring.barber.service.Service service = optionalService.get();

        if (!businessUserService.hasAccessToService(service)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You do not have permission to delete service: " + service.getName());
        }

        // Operating on List copy because elements of original List are being removed,
        // which would interrupt iteration
        List<Appointment> appointmentsToDelete = new ArrayList<>(service.getAppointments());
        for (Appointment appointment : appointmentsToDelete) {
            appointmentService.delete(appointment.getId());
        }

        service.getLocation().getServices().remove(service);

        serviceRepository.delete(service);
    }
}
