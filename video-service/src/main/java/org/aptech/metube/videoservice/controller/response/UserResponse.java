package org.aptech.metube.videoservice.controller.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserResponse {
    private Long id;
    private String fullName;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String address;
    private String avatar;
    private Integer countFollow;
    private Boolean isVerified;
}
