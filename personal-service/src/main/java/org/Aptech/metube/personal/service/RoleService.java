package org.aptech.metube.personal.service;

import org.aptech.metube.personal.exception.DuplicateException;
import org.aptech.metube.personal.exception.NotFoundException;
import org.aptech.metube.personal.controller.request.RoleCreateRequest;
import org.aptech.metube.personal.controller.request.RoleUpdateRequest;
import org.aptech.metube.personal.controller.response.RoleResponse;

import java.util.List;

public interface RoleService {
    Long save(RoleCreateRequest request) throws DuplicateException;
    Long update(RoleUpdateRequest request) throws NotFoundException, DuplicateException;
    List<RoleResponse> getAll();
    RoleResponse getById(Long id) throws NotFoundException;
    boolean delete(Long id) throws NotFoundException;
}
