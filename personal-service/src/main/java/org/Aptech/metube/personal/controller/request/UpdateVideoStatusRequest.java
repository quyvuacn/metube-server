package org.aptech.metube.personal.controller.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.aptech.metube.personal.constant.EntityStatusCode;

@Getter
@Setter
@Builder
public class UpdateVideoStatusRequest {
    private Long userId;
    private EntityStatusCode statusCode;
}
