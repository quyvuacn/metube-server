package org.aptech.metube.personal.dto;

import org.aptech.metube.personal.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@SuperBuilder
public class RoleDto{
    private Long id;
    private String name;
    private String description;
    List<User> userList;
}
