package org.aptech.metube.personal.service.impl;

import org.aptech.metube.personal.constant.EntityStatusCode;
import org.aptech.metube.personal.exception.DuplicateException;
import org.aptech.metube.personal.exception.NotFoundException;
import org.aptech.metube.personal.config.Translator;
import org.aptech.metube.personal.controller.request.RoleCreateRequest;
import org.aptech.metube.personal.controller.request.RoleUpdateRequest;
import org.aptech.metube.personal.controller.response.RoleResponse;
import org.aptech.metube.personal.entity.Role;
import org.aptech.metube.personal.entity.User;
import org.aptech.metube.personal.repository.RoleRepository;
import org.aptech.metube.personal.repository.UserRepository;
import org.aptech.metube.personal.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public Long save(RoleCreateRequest request) throws DuplicateException {
        if (roleRepository.findByName(request.getName()).isPresent()) {
            throw new DuplicateException(Translator.toLocale("role.name.existed"));
        }
        Role role = Role.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        Role result = roleRepository.save(role);
        return result.getId();
    }
    @Override
    public Long update(RoleUpdateRequest request) throws NotFoundException, DuplicateException {
        Role role = roleRepository.findById(request.getId())
                .orElseThrow(()-> new NotFoundException(Translator.toLocale("role.not.found")));
        Optional<Role> existedRole = roleRepository.findByName(request.getName());
        if (existedRole.get() != null && !existedRole.get().getId().equals(role.getId())){
            throw new DuplicateException(Translator.toLocale("role.name.existed"));
        }
        role.setName(request.getName());
        role.setDescription(request.getDescription());
        roleRepository.save(role);
        return role.getId();
    }
    @Override
    public List<RoleResponse> getAll(){
        return roleRepository.findAll().stream()
                .map(this::mapRoleToRoleResponse)
                .toList();
    }
    @Override
    public RoleResponse getById(Long id) throws NotFoundException {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Translator.toLocale("role.not.found")));
        RoleResponse result = mapRoleToRoleResponse(role);
        return result;
    }
    @Override
    public boolean delete(Long id) throws NotFoundException {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Translator.toLocale("role.not.found")));
        List<User> userAssignedRole = userRepository.findAllByRolesContains(role.getId());
        if (!CollectionUtils.isEmpty(userAssignedRole)){
            for (User user : userAssignedRole){
                List<Role> roles = user.getRoles();
                roles.remove(role);
                user.setRoles(roles);
                userRepository.save(user);
            }
        }
        role.setStatusCode(EntityStatusCode.DELETED);
        roleRepository.save(role);
        return true;
    }
    // map tay từ role sang roleResponse
    private RoleResponse mapRoleToRoleResponse(Role role) {
        RoleResponse roleResponse = new RoleResponse();
        roleResponse.setId(role.getId());
        roleResponse.setName(role.getName());
        roleResponse.setDescription(role.getDescription());
        return roleResponse;
    }
}
