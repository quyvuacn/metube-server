package org.aptech.metube.videoservice.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class UserAccountTypeResponse {
    private Long id;
    private Long userId;
    private Long typeId;
    private LocalDateTime expireDate;
    private String createdBy;
    private LocalDateTime createdDate;
    private String modifiedBy;
    private LocalDateTime modifiedDate;
}
