package org.aptech.metube.admin.config;

import org.aptech.metube.admin.controller.response.ApiResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(84000);
        factory.setConnectTimeout(84000);

        return new RestTemplate(factory);
    }

    public static <T, R> R callApiMethodGET (
            String apiUrl,
            String token,
            ParameterizedTypeReference<T> requestType,
            ParameterizedTypeReference<R> responseType,
            RestTemplate restTemplate) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<T> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<ApiResponse> response = restTemplate.exchange(apiUrl, HttpMethod.GET, requestEntity, ApiResponse.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            ApiResponse.Payload payload = response.getBody().getBody();
            if (payload != null) {
                return (R) payload.getData();
            }
        }
        return null;
    }
    public static <T, R> R callApiMethodPOST(
            String apiUrl,
            String token,
            T requestBody,
            ParameterizedTypeReference<R> responseType,
            RestTemplate restTemplate) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<T> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<ApiResponse> response = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, ApiResponse.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            ApiResponse.Payload payload = response.getBody().getBody();
            if (payload != null) {
                return (R) payload.getData();
            }
        }
        return null;
    }
    public static <T, R> R callApiMethodPUT(
            String apiUrl,
            String token,
            T requestBody,
            ParameterizedTypeReference<R> responseType,
            RestTemplate restTemplate) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<T> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<ApiResponse> response = restTemplate.exchange(apiUrl, HttpMethod.PUT, requestEntity, ApiResponse.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            ApiResponse.Payload payload = response.getBody().getBody();
            if (payload != null) {
                return (R) payload.getData();
            }
        }
        return null;
    }
    public static <R> R callApiMethodDELETE(
            String apiUrl,
            String token,
            ParameterizedTypeReference<R> responseType,
            RestTemplate restTemplate) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<ApiResponse> response = restTemplate.exchange(apiUrl, HttpMethod.DELETE, requestEntity, ApiResponse.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            ApiResponse.Payload payload = response.getBody().getBody();
            if (payload != null) {
                return (R) payload.getData();
            }
        }
        return null;
    }
}
