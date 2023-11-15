package org.aptech.metube.admin.controller;

import com.netflix.discovery.converters.Auto;
import lombok.extern.slf4j.Slf4j;
import org.aptech.metube.admin.config.RestTemplateConfig;
import org.aptech.metube.admin.config.Translator;
import org.aptech.metube.admin.controller.request.RoleCreateRequest;
import org.aptech.metube.admin.controller.response.ApiResponse;
import org.aptech.metube.admin.exception.DuplicateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/management/roles")
@Slf4j
public class RoleManagementController {
    @Autowired
    private RestTemplate restTemplate;  // Inject RestTemplate bean
    @Value("${base.url}")
    private String apiBaseUrl;

    @PostMapping(value = "/create")
    public ApiResponse create(@Valid @RequestBody RoleCreateRequest request) throws DuplicateException {
        log.info("Request Post/create role {}", request.getName());
        String apiUrl = apiBaseUrl + "/api/v1/roles/create";
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJxdWFuZ2FuaHVzZXJyciIsInJvbGVzIjpbIlNVUEVSX0FETUlOIiwiVVNFUiJdLCJpYXQiOjE2OTg2MzkzMTksImV4cCI6MTY5ODcyNTcxOX0.lTYxefKjriyk5iBoruIe861t7dSBA5ehr-JthNkW41-W8hAJppV6Zci4tzt2T7OTSMo8HtA9BgzNSweawtbaWQ";
        ParameterizedTypeReference<Integer> responseType = new ParameterizedTypeReference<>() {};

        Integer result = RestTemplateConfig.callApiMethodPOST(apiUrl, token, request, responseType, restTemplate);

        return new ApiResponse(HttpStatus.CREATED, Translator.toLocale("role.create.success"), result);
    }
}
