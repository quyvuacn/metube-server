package org.aptech.metube.personal.controller.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AssignRoleUserRequest {
    private Long roleId;
    private Long userId;
}
