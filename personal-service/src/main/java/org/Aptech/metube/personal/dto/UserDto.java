package org.aptech.metube.personal.dto;

import org.aptech.metube.personal.constant.EntityStatusCode;
import org.aptech.metube.personal.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class UserDto{
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
    private List<Role> roles;
    private EntityStatusCode statusCode;
}
