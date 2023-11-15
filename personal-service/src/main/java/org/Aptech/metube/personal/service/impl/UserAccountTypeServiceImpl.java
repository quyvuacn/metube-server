package org.aptech.metube.personal.service.impl;

import org.aptech.metube.personal.controller.response.UserAccountTypeResponse;
import org.aptech.metube.personal.entity.UserAccountType;
import org.aptech.metube.personal.repository.UserAccountTypeRepository;
import org.aptech.metube.personal.service.UserAccountTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAccountTypeServiceImpl implements UserAccountTypeService {
    @Autowired
    UserAccountTypeRepository userAccountTypeRepository;
    @Override
    public List<UserAccountTypeResponse> getUserAccountTypesByUserId(Long id) {
        List<UserAccountType> userAccountTypeList = userAccountTypeRepository.findByUserId(id);
        return userAccountTypeList.stream()
                .map(userAccountType -> {
                    UserAccountTypeResponse response = UserAccountTypeResponse.builder()
                            .id(userAccountType.getId())
                            .typeId(userAccountType.getTypeId())
                            .userId(userAccountType.getUserId())
                            .createdDate(userAccountType.getCreatedDate())
                            .expireDate(userAccountType.getExpireDate())
                            .build();
                    return response;
                }).toList();
    }

}
