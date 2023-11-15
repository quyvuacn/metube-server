package org.aptech.metube.personal.controller.request;

import lombok.Getter;
import lombok.Setter;
import org.aptech.metube.personal.constant.EntityStatusCode;

@Getter
@Setter
public class AdminUpdateUserStatusRequest {
    private Long userId;
    private EntityStatusCode statusCode;
}
