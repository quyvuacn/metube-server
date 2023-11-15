package org.aptech.metube.personal.security.payload.request;


import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class SignupRequest {
    private String fullName;
    private String username;
    private String tel;
    private String email;
    private String password;
    private String roleName;
}
