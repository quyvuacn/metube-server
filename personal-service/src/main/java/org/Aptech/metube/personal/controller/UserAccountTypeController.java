package org.aptech.metube.personal.controller;

import lombok.extern.slf4j.Slf4j;
import org.aptech.metube.personal.config.Translator;
import org.aptech.metube.personal.controller.response.ApiResponse;
import org.aptech.metube.personal.controller.response.UserAccountTypeResponse;
import org.aptech.metube.personal.exception.NotFoundException;
import org.aptech.metube.personal.service.UserAccountTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/account-type")
@Slf4j(topic = "ACCOUNT-TYPE")
public class UserAccountTypeController {
    @Autowired
    UserAccountTypeService userAccountTypeService;
    @GetMapping("/user/{id}")
    public ApiResponse getByUserId (@PathVariable(value = "id") Long id) throws NotFoundException {
        log.info("Request GET/list user account type by userId: {}", id);
        if (id == null){
            throw new NotFoundException(Translator.toLocale("user.not.found"));
        }
        List<UserAccountTypeResponse> result = userAccountTypeService.getUserAccountTypesByUserId(id);
        return new ApiResponse(HttpStatus.OK, Translator.toLocale("success"), result);
    }
}
