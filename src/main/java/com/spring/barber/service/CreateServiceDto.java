package com.spring.barber.service;

import com.spring.barber.location.LocationExist;
import com.spring.barber.location.LocationRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
public class CreateServiceDto {

    @Autowired
    private LocationRepository locationRepository;

    @NotEmpty(message = "'name' must not be empty")
    private String name;

    @NotEmpty(message = "'description' must not be empty")
    private String description;

    @Min(value = 0, message = "'price' value must be at least 0")
    @NotNull(message = "'price' cannot be null")
    private int price;

    @Min(value = 1, message = "'duration' value must be at least 1")
    @NotNull(message = "'duration' cannot be null")
    private int duration;

    @LocationExist
    @Positive(message = "'locationId' must be positive integer")
    @NotNull(message = "'locationId' cannot be null")
    private long locationId;

    public Service convertToEntity() {
        Service service = new Service();
        service.setName(this.getName());
        service.setDescription(this.getDescription());
        service.setPrice(this.getPrice());
        service.setDuration(this.getDuration());
        return service;
    }

}
