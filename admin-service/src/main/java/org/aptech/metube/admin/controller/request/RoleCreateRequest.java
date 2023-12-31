package org.aptech.metube.admin.controller.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class RoleCreateRequest implements Serializable {
    private String name;
    private String description;
}
