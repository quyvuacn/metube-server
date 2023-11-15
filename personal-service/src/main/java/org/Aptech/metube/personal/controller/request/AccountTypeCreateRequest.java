package org.aptech.metube.personal.controller.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
@Builder
public class AccountTypeCreateRequest {
    private String name;
    private Integer price;
    private int activeTime;
}
