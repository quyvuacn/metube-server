package org.aptech.metube.personal.mapper;

import org.aptech.metube.personal.dto.UserDto;
import org.aptech.metube.personal.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User dtoToEntity (UserDto u);
    UserDto entityToDto (User u);
}
