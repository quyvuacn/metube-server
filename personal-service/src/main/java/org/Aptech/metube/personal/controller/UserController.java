package org.aptech.metube.personal.controller;

import org.aptech.metube.personal.config.Translator;
import org.aptech.metube.personal.controller.request.AdminUpdateUserStatusRequest;
import org.aptech.metube.personal.controller.request.AssignRoleUserRequest;
import org.aptech.metube.personal.controller.request.UserUpdateRequest;
import org.aptech.metube.personal.controller.response.ApiResponse;
import org.aptech.metube.personal.controller.response.ErrorResponse;
import org.aptech.metube.personal.controller.response.UserResponse;
import org.aptech.metube.personal.dto.UserDto;
import org.aptech.metube.personal.exception.NotFoundException;
import org.aptech.metube.personal.exception.RequestValidException;
import org.aptech.metube.personal.service.UserFollowService;
import org.aptech.metube.personal.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("api/v1/users")
@Slf4j
public class UserController {
    @Autowired
    UserServiceImpl userService;
    @Autowired
    UserFollowService userFollowService;

    @GetMapping("/list-all")
    public ApiResponse getAll(@RequestParam(required = false) String search,
                              @RequestParam(defaultValue = "0") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize){
        LinkedHashMap<String, Object> result = userService.findAll(search, pageNum, pageSize);
        return new ApiResponse(OK, Translator.toLocale("get-all.user.success"), result);
    }
    @GetMapping("/get/{id}")
    public ApiResponse getUserById(@PathVariable Long id) throws NotFoundException {
        log.info("Request GET/user by id: {}", id);
        if (id == null){
            throw new RequestValidException(Translator.toLocale("invalid.param"));
        }

        UserResponse result = userService.findById(id);

        return new ApiResponse(OK, Translator.toLocale("get.user.success"), result);
    }
    @GetMapping(value = "/get-me")
    public ApiResponse getUserInfo(){
        log.info("Request GET /users/getMe");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return new ErrorResponse(BAD_REQUEST, Translator.toLocale("forbidden"));
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        UserResponse result = userService.findByUsername(username);

        return new ApiResponse(OK, Translator.toLocale("success"), result);
    }
    @PutMapping(value = "/update-info")
    public ApiResponse update(@RequestBody @Valid UserUpdateRequest userUpdateRequest) throws NotFoundException {

        log.info("Request POST /user/update");
        if (userUpdateRequest.getId() == null) {
            return new ErrorResponse(BAD_REQUEST, Translator.toLocale("invalid.param"));
        }
        Long result = userService.updateInfo(userUpdateRequest);
        return new ApiResponse(OK, Translator.toLocale("success"), result);
    }
    @PostMapping(value = "/follows/follow/{id}")
    public ApiResponse follow (@PathVariable Long id) throws NotFoundException {
        log.info("Request POST/follow user: {}", id);
        if (id == null){
            throw new NotFoundException(Translator.toLocale("user.not.found"));
        }
        Long result = userService.followUser(id);
        return new ApiResponse(OK, Translator.toLocale("success"), result);
    }
    @PostMapping(value = "/follows/check/{id}")
    public ApiResponse getListFollowing (@PathVariable Long id) throws NotFoundException {
        log.info("Request check followed of user: {}", id);
        Boolean check = userFollowService.checkFollowedUser(id);
        return new ApiResponse(OK, Translator.toLocale("success"), check);
    }
    @PostMapping(value = "/follows/unfollow/{id}")
    public ApiResponse unfollow (@PathVariable Long id) throws NotFoundException {
        log.info("Request POST/follow user: {}", id);
        if (id == null){
            throw new NotFoundException(Translator.toLocale("user.not.found"));
        }
        Long result = userService.unFollowUser(id);
        return new ApiResponse(OK, Translator.toLocale("success"), result);
    }
    @PutMapping(value = "/update-status")
    public ApiResponse blockUser(@Valid @RequestBody AdminUpdateUserStatusRequest request, HttpServletRequest httpServletRequest) throws NotFoundException {
        log.info("Request PUT/block user: {} ", request.getUserId());
        if (request.getStatusCode() == null || request.getUserId() == null){
            throw new RequestValidException(Translator.toLocale("invalid.param"));
        }
        Long result = userService.blockUser(request, httpServletRequest);
        return new ApiResponse(OK, Translator.toLocale("user.block.success"), result);
    }
    @PostMapping(value = "/assign-role")
    public ApiResponse assignRole(@RequestBody AssignRoleUserRequest request) throws NotFoundException {
        log.info("Request POST/assign role for user : {}", request.getUserId());
        Long result = userService.assignRole(request);
        return new ApiResponse(OK, Translator.toLocale("success"), result);
    }
}
