package org.aptech.metube.personal.controller.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleCreateRequest {
    private String name;
    private String description;
}
