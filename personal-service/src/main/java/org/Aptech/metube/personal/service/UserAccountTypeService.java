package org.aptech.metube.personal.service;

import org.aptech.metube.personal.controller.response.UserAccountTypeResponse;
import org.aptech.metube.personal.entity.UserAccountType;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserAccountTypeService {
    List<UserAccountTypeResponse> getUserAccountTypesByUserId(Long id);
}
