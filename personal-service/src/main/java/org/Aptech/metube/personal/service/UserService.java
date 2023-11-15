package org.aptech.metube.personal.service;

import org.aptech.metube.personal.controller.request.AdminUpdateUserStatusRequest;
import org.aptech.metube.personal.controller.response.UserResponse;
import org.aptech.metube.personal.exception.NotFoundException;
import org.aptech.metube.personal.controller.request.AssignRoleUserRequest;
import org.aptech.metube.personal.controller.request.UserUpdateRequest;
import org.aptech.metube.personal.dto.UserDto;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;

public interface UserService {

    UserResponse findById(Long id) throws NotFoundException;
    UserDto findByEmail(String email);
    Long updateInfo (UserUpdateRequest request) throws NotFoundException;
    Long followUser (Long userBeingFollowedId) throws NotFoundException;
    Long unFollowUser (Long userBeingFollowedId) throws NotFoundException;
    Long blockUser (AdminUpdateUserStatusRequest request, HttpServletRequest httpServletRequest) throws NotFoundException;
    Long assignRole(AssignRoleUserRequest request) throws NotFoundException;
    UserResponse findByUsername(String username);
    LinkedHashMap<String, Object> findAll(String search, Integer pageNum, Integer pageSize);
}
