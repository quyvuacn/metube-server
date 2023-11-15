package org.aptech.metube.personal.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Date;

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
