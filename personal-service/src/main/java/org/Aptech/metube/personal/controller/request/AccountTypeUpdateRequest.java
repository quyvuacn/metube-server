package org.aptech.metube.personal.controller.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AccountTypeUpdateRequest {
    private Long id;
    private String name;
    private Integer price;
    private Integer activeTime;
}
