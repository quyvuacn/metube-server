package org.aptech.metube.personal.controller.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {
    private Long id;
    private String fullName;
    private String username;
    private String password;
    private String email;
    private String avatar;
    private String phone;
    private String address;
}
