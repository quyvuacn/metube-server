package org.aptech.metube.personal.controller.response;

import org.springframework.http.HttpStatus;

public class ErrorResponse extends ApiResponse {
    /**
     * Error response for api
     *
     * @param status
     * @param message
     */
    public ErrorResponse(HttpStatus status, String message) {
        super(status, message, null);
    }
}
