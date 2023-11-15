package org.aptech.metube.personal.mapper;

import org.aptech.metube.personal.dto.RoleDto;
import org.aptech.metube.personal.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role dtoToEntity (RoleDto dto);
    RoleDto entityToDto (Role role);
}
