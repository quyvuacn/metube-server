package org.aptech.metube.personal.controller;

import lombok.extern.slf4j.Slf4j;
import org.aptech.metube.personal.config.Translator;
import org.aptech.metube.personal.controller.request.AssignRoleUserRequest;
import org.aptech.metube.personal.controller.request.RoleCreateRequest;
import org.aptech.metube.personal.controller.request.RoleUpdateRequest;
import org.aptech.metube.personal.controller.response.ApiResponse;
import org.aptech.metube.personal.controller.response.RoleResponse;
import org.aptech.metube.personal.exception.DuplicateException;
import org.aptech.metube.personal.exception.NotFoundException;
import org.aptech.metube.personal.service.impl.RoleServiceImpl;
import org.aptech.metube.personal.service.impl.UserServiceImpl;
import org.aptech.metube.personal.exception.RequestValidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/roles")
@Slf4j
public class RoleController {
    @Autowired
    RoleServiceImpl roleService;
    @Autowired
    UserServiceImpl userService;

    @PostMapping(value = "/create")
    public ApiResponse create(@Valid @RequestBody RoleCreateRequest request) throws DuplicateException {
        log.info("Request Post/create role {}", request.getName());
        Long result = roleService.save(request);
        return new ApiResponse(HttpStatus.CREATED, Translator.toLocale("role.create.success"), result);
    }

    @PutMapping(value = "/update")
    public ApiResponse update(@Valid @RequestBody RoleUpdateRequest request) throws DuplicateException, NotFoundException {
        log.info("Request Put/update role {}", request.getId());
        Long result = roleService.update(request);
        return new ApiResponse(HttpStatus.OK, Translator.toLocale("role.update.success"), result);
    }

    @GetMapping(value = "/list")
    public ApiResponse getAll(){
        log.info("Request GET/get all role");
        List<RoleResponse> result = roleService.getAll();
        return new ApiResponse(HttpStatus.OK, Translator.toLocale("role.get-list.success"), result);
    }

    @GetMapping(value = "/{id}")
    public ApiResponse getById(@PathVariable Long id) throws NotFoundException {
        log.info("Request GET/get role by id, {}", id);
        if (null == id){
            throw new RequestValidException(Translator.toLocale("invalid.param"));
        }
        RoleResponse result = roleService.getById(id);
        return new ApiResponse(HttpStatus.OK, Translator.toLocale("role.get.success"), result);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ApiResponse deleteById(@PathVariable Long id) throws NotFoundException {
        log.info("Request DELETE/role by id {}", id);
        if (null == id){
            throw new RequestValidException(Translator.toLocale("invalid.param"));
        }
        boolean result = roleService.delete(id);
        return new ApiResponse(HttpStatus.OK, Translator.toLocale("role.delete.success"), result);
    }

    @PostMapping(value = "/assign-role")
    public ApiResponse assignRole(@RequestBody AssignRoleUserRequest request) throws NotFoundException, RequestValidException {
        log.info("Request Post/assign role for user {}", request.getUserId());
        if (request.getRoleId() == null || request.getUserId() == null){
            throw new RequestValidException(Translator.toLocale("invalid.param"));
        }
        Long result = userService.assignRole(request);
        return new ApiResponse(HttpStatus.OK, Translator.toLocale("role.assign.success"), result);
    }
}
