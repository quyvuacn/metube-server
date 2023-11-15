package org.aptech.metube.personal.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.aptech.metube.personal.constant.EntityStatusCode;
import org.aptech.metube.personal.entity.AccountType;
import org.aptech.metube.personal.entity.Role;
import org.aptech.metube.personal.entity.UserAccountType;

import java.util.List;

@Getter
@Setter
@Builder
public class UserResponse {
    private Long id;
    private String fullName;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String address;
    private String avatar;
    private EntityStatusCode statusCode;
    private Integer countFollow;
    private Boolean isVerified;
    private List<Role> roles;
    private List<UserAccountType> accountTypes;
}
