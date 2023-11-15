package org.aptech.metube.gateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> openApiEndpoints = List.of(
            "/api/v1/auth/register",
            "/api/v1/auth/login",
            "/auth/token",
            "/eureka",
            "/api/v1/video/list-video",
            "/api/v1/users/get/",
            "/api/v1/video/list-video/category/",
            "/api/v1/category/list-categories",
            "/api/v1/video/user/latest/",
            "/api/v1/video/get"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));

}
