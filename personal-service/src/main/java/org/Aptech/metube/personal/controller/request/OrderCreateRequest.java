package org.aptech.metube.personal.controller.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class OrderCreateRequest {
    private Long id;
    private int unit;
    private int amount;
    private String status;
    private Long accountTypeId;
    private String payer;
    private String payee;
    private Long userId;
    private String address;
    private LocalDateTime createDate;
}
