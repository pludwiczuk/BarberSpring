package com.spring.barber.businessUser;

import com.spring.barber.business.Business;
import com.spring.barber.user.UserResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessUserResponse {

    private Long id;
    private UserResponse user;
    private Business business;

    public BusinessUserResponse(BusinessUser businessUser) {
        this.id = businessUser.getId();
        this.user = new UserResponse(businessUser.getUser());
        this.business = businessUser.getBusiness();
    }

}
