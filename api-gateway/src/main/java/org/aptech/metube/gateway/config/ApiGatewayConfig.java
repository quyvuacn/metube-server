//package org.aptech.metube.gateway.config;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.gateway.filter.GatewayFilter;
//import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
//import org.springframework.cloud.gateway.route.RouteLocator;
//import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.config.web.server.ServerHttpSecurity;
//import org.springframework.security.web.server.SecurityWebFilterChain;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//import org.springframework.web.server.ServerWebExchange;
//
//import java.util.List;
//
//@Configuration
//public class ApiGatewayConfig {
//
//    @Bean
//    public RouteLocator customRouteLocator(RouteLocatorBuilder builder, AuthorizationFilter authorizationFilter) {
//        return builder.routes()
//                .route("VIDEO-SERVICE", r -> r
//                        .path("/video/**")
//                        .filters(f -> f.filter(authorizationFilter.apply(new AuthorizationFilter.Config())))
//                        .uri("lb://VIDEO-SERVICE"))
//                .route("PERSONAL-SERVICE", r -> r
//                        .path("/users/**", "/users**")
//                        .filters(f -> f.filter(authorizationFilter.apply(new AuthorizationFilter.Config())))
//                        .uri("lb://PERSONAL-SERVICE"))
//                .route("PERSONAL-SERVICE-AUTH", r -> r
//                        .path("/auth/**", "/auth**")
//                        .uri("lb://PERSONAL-SERVICE"))
//                .build();
//    }
//
//
//    @Component
//    public static class AuthorizationFilter extends AbstractGatewayFilterFactory<AuthorizationFilter.Config> {
//
//        @Autowired
//        private SecurityConfig securityConfig;
//        @Value("${role.user-only}")
//        private String USERONLY;
//        @Value("${role.admin-only}")
//        private String ADMINONLY;
//        @Value("${role.manager-only}")
//        private String MANAGERONLY;
//
//        public AuthorizationFilter() {
//            super(Config.class);
//        }
//
//        @Override
//        public GatewayFilter apply(Config config) {
//            return (exchange, chain) -> {
//                String accessToken = extractAccessToken(exchange);
//                if (accessToken != null && accessToken.startsWith("Bearer")) {
//                    accessToken = accessToken.substring(7);
//                    if (securityConfig.hasRequiredRoles(accessToken, USERONLY)) {
//                        return chain.filter(exchange);
//                    }
//                }
//                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
//                return exchange.getResponse().setComplete();
//            };
//        }
//
//        private String extractAccessToken(ServerWebExchange exchange) {
//            String headerAuth = exchange.getRequest().getHeaders().getFirst("Authorization");
//
//            if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
//                return headerAuth;
//            }
//
//            return null;
//        }
//
//        public static class Config {
//        }
//    }
//
//    @Component
//    public static class SecurityConfig {
//
//        private String jwtSecretKey = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789hgadgjadhhcvhsjhdjsdgahgdcgfdahcgjsgdhsdvcdhdjcgdvacvag";
//
//
//        public List<?> getRolesFromJwtToken(String token) {
//            Claims claims = Jwts.parser()
//                    .setSigningKey(jwtSecretKey)
//                    .parseClaimsJws(token)
//                    .getBody();
//
//            return claims.get("roles", List.class);
//        }
//
//        public boolean hasRequiredRoles(String accessToken, String... requiredRoles) {
//            try {
//                List<String> userRoles = (List<String>) this.getRolesFromJwtToken(accessToken);
//
//                for (String requiredRole : requiredRoles) {
//                    if (userRoles.contains(requiredRole)) {
//                        return true;
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return false;
//        }
//
//    }
//    @Bean
//    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http){
//        http.csrf().disable();
//        return http.build();
//    }
//}